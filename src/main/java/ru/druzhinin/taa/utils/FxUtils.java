package ru.druzhinin.taa.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class FxUtils {
    private static final Logger logger = LogManager.getLogger(FxUtils.class);

    public static void loadFxmlAndController(Parent parent, String resourceName)
    {
        long startMills = System.currentTimeMillis();

        FXMLLoader loader = new FXMLLoader(MyApplication.class.getResource("fxml/" + resourceName + ".fxml"));
        //FXMLLoader loader = new FXMLLoader(FxUtils.class.getResource("fxml/" + resourceName + ".fxml"));
        loader.setRoot(parent);
        loader.setController(parent);

        try {
            loader.load();
        } catch (Exception e) {
            logger.error("Error while loading FXML & set up controller: " +
                    parent.getClass().getName() + " " + resourceName, e);
        }

        logger.debug("Form '" + parent.getClass().getSimpleName() + "' loaded by " +
                (System.currentTimeMillis() - startMills) + "ms");
    }

    public static void createPopupStage(Stage mainStage, String title, Parent root)
    {
        long startMills = System.currentTimeMillis();

        Stage stage = new Stage();
        stage.setTitle(title == null ? mainStage.getTitle() : title);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.getIcons().addAll(mainStage.getIcons());
        stage.setScene(scene);

        setStageSizesFromRootElement(stage);
        stage.show();

        logger.debug("Create popup stage with '" + root.getClass().getSimpleName() + "' form by " + (System.currentTimeMillis() - startMills) + "ms");
    }

    public static void createPopupStageLater(Stage mainStage, String title, Parent root)
    {
        Platform.runLater(() -> createPopupStage(mainStage, title, root));
    }

    public static void createPopupStage(Stage mainStage, Parent root)
    {
        createPopupStage(mainStage, null, root);
    }

    public static void createPopupStageLater(Stage mainStage, Parent root)
    {
        Platform.runLater(() -> createPopupStage(mainStage, root));
    }

    public static void closeElementStage(Parent element)
    {
        ((Stage)element.getScene().getWindow()).close();
    }

    public static void closeElementStageLater(Parent element)
    {
        Platform.runLater(() -> closeElementStage(element));
    }

    public static void setStageSizesFromRootElement(Stage stage)
    {
        if(stage.getScene() == null) {
            logger.warn("Can't set stage sizes because scene is null");
            return;
        }

        Parent root = stage.getScene().getRoot();
        if(root == null) {
            logger.warn("Can't set stage sizes because scene's root is null");
            return;
        }

        double minWidth = root.minWidth(-1);
        double minHeight = root.minHeight(-1);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        double maxWidth = root.maxWidth(-1);
        double maxHeight = root.maxHeight(-1);
        if(maxWidth <= minWidth && maxHeight <= minHeight) {
            stage.setResizable(false);
        } else {
            stage.setResizable(true);
            stage.setMaxWidth(maxWidth);
            stage.setMaxHeight(maxHeight);
        }

        if(stage.isMaximized()) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(root.prefWidth(-1));
            stage.setHeight(root.prefHeight(-1));
        }

        stage.centerOnScreen();
    }

    public static <T> void checkComboBox(ComboBox<T> comboBox)
    {
        List<T> listToRemove = new ArrayList<>();
        comboBox.getItems().stream()
                .filter(item -> String.valueOf(item).equals(OtherUtils.DEPRECATED_ELEMENT))
                .forEach(item -> listToRemove.add(item));
        comboBox.getItems().removeAll(listToRemove);
    }

    public static void changeCursorOnAllStages(Cursor cursor)
    {
        Window.getWindows().forEach(stage -> {
            Scene scene = stage.getScene();
            if(scene != null) {
                scene.setCursor(cursor);
            }
        });
    }

    public static void setZeroAnchorPaneConstrains(Node node)
    {
        AnchorPane.setTopAnchor(node, 0D);
        AnchorPane.setRightAnchor(node, 0D);
        AnchorPane.setBottomAnchor(node, 0D);
        AnchorPane.setLeftAnchor(node, 0D);
    }

    public static void setAllSizes(Region region, double width, double height)
    {
        region.setMinWidth(width);
        region.setPrefWidth(width);
        region.setMaxWidth(width);

        region.setMinHeight(height);
        region.setPrefHeight(height);
        region.setMaxHeight(height);
    }

    public static void setAllWidths(Region region, double width)
    {
        region.setMinWidth(width);
        region.setPrefWidth(width);
        region.setMaxWidth(width);
    }

    public static void setAllHeights(Region region, double height)
    {
        region.setMinHeight(height);
        region.setPrefHeight(height);
        region.setMaxHeight(height);
    }
}
