package ru.druzhinin.taa;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.controller.LoginForm;
import ru.druzhinin.taa.utils.Log4jUtil;

public class MyApplication extends Application {
    static {
        Log4jUtil.configureLogging( System.getProperty("user.home") + "/IdeaProjects/diplom/", "Application");
    }

    private static final Logger logger = LogManager.getLogger(MyApplication.class);

    @Override
    public void start(Stage stage) {
        stage.setTitle("TAA [trend analysis application]");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image/logo.png")));
        Scene scene = new Scene(new LoginForm(), 600, 400);
        stage.setScene(scene);
        stage.show();

        logger.debug("Application started");
    }

    public static void exit()
    {
        logger.debug("Disabling application...");
        Platform.exit();
    }

    public static void main(String[] args) {
        launch();
    }
}