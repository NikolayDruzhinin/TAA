package ru.druzhinin.taa.controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.MyApplication;

public class MainMenuForm extends AnchorPane {
    @FXML
    protected void onInputDataButton(){
        Platform.runLater(() -> {
            logger.debug("Connection to Db is successful");
            Stage stage = new Stage();
            stage.setTitle("TAA [trend analysis application]");
            stage.getIcons().add(new Image(MyApplication.class.getResourceAsStream("image/logo.png")));
            stage.setScene(new Scene(new InsertDataForm(), 800, 600));
            stage.show();
            MainMenuForm.this.getScene().getWindow().hide();
        });
    }

    @FXML
    protected void onLoadExcelDataButton() {
    }

    @FXML
    protected void onTrendAnalysisButton() {
        Platform.runLater(() -> {
            logger.debug("Connection to Db is successful");
            Stage stage = new Stage();
            stage.setTitle("TAA [trend analysis application]");
            stage.getIcons().add(new Image(MyApplication.class.getResourceAsStream("image/logo.png")));
            stage.setScene(new Scene(new GetStatisticsForm(), 1024, 800));
            stage.show();
            MainMenuForm.this.getScene().getWindow().hide();
        });
    }

    @FXML
    protected void onLogButton() {
    }

    private static final Logger logger = LogManager.getLogger(MainMenuForm.class);

    public MainMenuForm() {
        FXMLLoader loader = new FXMLLoader(MyApplication.class.getResource("fxml/MainMenuForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        } catch (Exception e) {
            logger.error("Error while loading FXML & set up controller MainMenuForm: " , e);
        }
    }
}
