package ru.druzhinin.taa.utils;

import javafx.application.Platform;
import javafx.scene.ImageCursor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AsyncTask {
    private static final Logger logger = LogManager.getLogger(AsyncTask.class);

    private static AtomicInteger taskCounter = new AtomicInteger(0);
    private static volatile Boolean singleTaskActive = false;

    private Action onStart = () -> FxUtils.changeCursorOnAllStages(ImageCursor.WAIT);
    private Action onFinish = () -> FxUtils.changeCursorOnAllStages(ImageCursor.DEFAULT);

    private Consumer<Exception> exceptionHandler = e ->
    {
        String message = e.getMessage();
        DialogUtil.showErrorLater("Ошибка: " + message);
    };

    private ActionWithException doInBackground;

    private boolean single;

    public AsyncTask(ActionWithException doInBackground) {
        this.doInBackground = doInBackground;
    }

    public static AsyncTask create(ActionWithException doInBackground)
    {
        return new AsyncTask(doInBackground);
    }

    public boolean runSingle()
    {
        synchronized (singleTaskActive)
        {
            if (singleTaskActive) {
                return false;
            }
            singleTaskActive = true;
        }

        this.single = true;
        this.start();
        return true;
    }

    public void run()
    {
        this.start();
    }

    private void start()
    {
        Thread thread = new Thread(() ->
        {
            if(onStart != null) {
                Platform.runLater(() -> onStart.action());
            }

            try {
                doInBackground.action();
            } catch (Exception e) {
                if(exceptionHandler != null) {
                    exceptionHandler.accept(e);
                    logger.debug("Handled exception in " + Thread.currentThread().getName(), e);
                } else {
                    logger.error("Unhandled exception in " + Thread.currentThread().getName(), e);
                    throw new RuntimeException("Exception in async task", e);
                }
            } finally {
                finishSingle();
            }

            if(onFinish != null) {
                Platform.runLater(() -> onFinish.action());
            }
        });

        thread.setDaemon(true);
        thread.setName("AsyncTaskThread-" + taskCounter.incrementAndGet());
        thread.start();
    }

    private void finishSingle()
    {
        if(this.single) {
            synchronized (singleTaskActive) {
                singleTaskActive = false;
            }
        }
    }

    public Action getOnStart() {
        return onStart;
    }

    public AsyncTask setOnStart(Action onStart) {
        this.onStart = onStart;
        return this;
    }

    public Action getOnFinish() {
        return onFinish;
    }

    public AsyncTask setOnFinish(Action onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public ActionWithException getDoInBackground() {
        return doInBackground;
    }

    public AsyncTask setDoInBackground(ActionWithException doInBackground) {
        this.doInBackground = doInBackground;
        return this;
    }

    public Consumer<Exception> getExceptionHandler() {
        return exceptionHandler;
    }

    public AsyncTask setExceptionHandler(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public static boolean isSingleTaskActive() {
        return singleTaskActive;
    }

    public static boolean busyWaitForSingleTask(long maxMills) {
        long startMills = System.currentTimeMillis();
        while(System.currentTimeMillis() - startMills < maxMills) {
            if(!singleTaskActive) {
                return true;
            }
            OtherUtils.sleep(10);
        }
        return false;
    }
}
