package ru.druzhinin.taa.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.MyApplication;
import ru.druzhinin.taa.entity.EmployeeEntity;
import ru.druzhinin.taa.entity.LogEntity;
import ru.druzhinin.taa.entity.ParameterEntity;
import ru.druzhinin.taa.entity.ProductEntity;
import ru.druzhinin.taa.utils.AsyncTask;
import ru.druzhinin.taa.utils.DialogUtil;
import ru.druzhinin.taa.utils.MyException;

import java.sql.SQLException;

public class LoginForm extends VBox {
    @FXML
    private TextField loginField;

    @FXML
    protected void onLoginButton() {
        this.doLogin();
    }

    @FXML
    private PasswordField passwordField;

    private static final Logger logger = LogManager.getLogger(LoginForm.class);

    public LoginForm() {
        FXMLLoader loader = new FXMLLoader(MyApplication.class.getResource("fxml/LoginForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
            Platform.runLater(() -> {
                LoginForm.this.getScene().setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        doLogin();
                    }
                });
            });
        } catch (Exception e) {
            logger.error("Error while loading FXML & set up controller LoginForm: " , e);
        }
    }

    @Getter
    @Setter
    private static EmployeeEntity authUser;

    private void doLogin()
    {
        if (loginField.getText().isEmpty()) {
            DialogUtil.showError("Введите логин");
            return;
        }

        if (passwordField.getText().isEmpty()) {
            DialogUtil.showError("Введите пароль");
            return;
        }

        authUser = new EmployeeEntity();
        authUser.setLogin(loginField.getText());
        authUser.setPassword(passwordField.getText());
        try{
            authUser.loadData();
            LogEntity le = new LogEntity("Авторизация", authUser);
            le.makeInsert(le.insertUserStatement());

            AsyncTask.create(() -> {
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    stage.setTitle("TAA [trend analysis application]");
                    stage.getIcons().add(new Image(MyApplication.class.getResourceAsStream("image/logo.png")));
                    stage.setScene(new Scene(new MainMenuForm(), 600, 400));
                    stage.show();
                    LoginForm.this.getScene().getWindow().hide();
                });
                logger.debug("MainMenuForm.fxml was loaded");
            }).runSingle();
        } catch (MyException e) {
            DialogUtil.showErrorLater("пользователь не найден в базе данных");
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
            DialogUtil.showErrorLater("Неверное имя пользователя/пароль");
        }
    }
}