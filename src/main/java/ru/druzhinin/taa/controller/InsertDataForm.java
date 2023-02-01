package ru.druzhinin.taa.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.MyApplication;
import ru.druzhinin.taa.entity.*;
import ru.druzhinin.taa.utils.*;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

import static ru.druzhinin.taa.entity.LogEntity.selectStatementTableView;
import static ru.druzhinin.taa.utils.ControlsUtil.loadComboBoxData;
import static ru.druzhinin.taa.utils.ControlsUtil.loadTableView;

public class InsertDataForm extends AnchorPane implements Initializable {
    @FXML
    private ComboBox<String> productNameBox;
    @FXML
    private RadioButton newProductNameRadioButton;
    @FXML
    private TextField newProductTextField;
    @FXML
    private ComboBox<String> productBatchBox;
    @FXML
    private RadioButton newProductBatchRadioButton;
    @FXML
    private TextField newProductBatchTextField;
    @FXML
    private ComboBox<String> releaseFormBox;
    @FXML
    private DatePicker productionDate;
    @FXML
    private TextField batchSizeTextField;
    @FXML
    private ComboBox<String> unitMeasurementBox;
    @FXML
    private ComboBox<String> parameterNameBox;
    @FXML
    private RadioButton newParamNameRadioButton;
    @FXML
    private TextField newParameterTextField;
    @FXML
    private TextField parameterValue;
    @FXML
    private ComboBox<String> boxParameterUnits;
    @FXML
    private Button inputData;
    @FXML
    private Button goBack;
    @FXML
    private TextField newParamMeasureTextField;
    @FXML
    private RadioButton newParamMeasureRadioButton;
    @FXML
    private TextField newReleaseFormTextField;
    @FXML
    private RadioButton newReleaseFormRadioButton;
    @FXML
    private RadioButton newProductMeasureRadioButton;
    @FXML
    private TextField newProductMeasuretextField;

    @FXML
    private TableView tableView;

    private static ProductEntity chosenProduct;
    private static ParameterEntity chosenParameter;
    private static LogEntity log;
    private static MeasurementUnitEntity productMeasure;
    private static MeasurementUnitEntity parameterMeasure;
    private static ReleaseFormEntity productReleaseForm;
    ComboControl releaseFormCombo;
    ComboControl productMeasureCombo;
    ParameterComboControl parameterCombo;
    ProductComboControl productCombo;
    ComboControl parameterMeasureCombo;
    ComboControl productBatch;

    @FXML
    protected void onInputDataAction(ActionEvent event) {
        doInput();
    }

    @FXML
    protected void onGoBackAction(ActionEvent event) {
        doGoBack();
    }

    private static final Logger logger = LogManager.getLogger(InsertDataForm.class);

    public void addListenerRadio(RadioButton button, Control enable, Control disable) {
        button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            enable.setDisable(!isNowSelected);
            disable.setDisable(isNowSelected);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        releaseFormCombo = new ComboControl("3. Форма выпуска", newReleaseFormRadioButton, newReleaseFormTextField, releaseFormBox);
        productMeasureCombo = new ComboControl("6. Ед. измерения", newProductMeasureRadioButton, newProductMeasuretextField, unitMeasurementBox);
        parameterCombo = new ParameterComboControl("7. Наименование параметра", newParamNameRadioButton, newParameterTextField, parameterNameBox, parameterValue);
        parameterMeasureCombo = new ComboControl("8. Ед. измерения", newParamMeasureRadioButton, newParamMeasureTextField, boxParameterUnits);
        productBatch = new ComboControl("2. Серия", newProductBatchRadioButton, newProductBatchTextField, productBatchBox);
        productCombo = new ProductComboControl("1. Наименование препарата", newProductNameRadioButton, newProductTextField, productNameBox, productionDate, batchSizeTextField, productBatch);
        productMeasure = new MeasurementUnitEntity();
        productReleaseForm = new ReleaseFormEntity();
        chosenProduct = new ProductEntity();
        chosenProduct.setProductCountUnit(productMeasure);
        chosenProduct.setReleaseForm(productReleaseForm);

        parameterMeasure = new MeasurementUnitEntity();
        chosenParameter = new ParameterEntity();
        chosenParameter.setProductId(chosenProduct);
        chosenParameter.setParameterCountUnit(parameterMeasure);

        log = new LogEntity();
        log.setProductId(chosenProduct);
        log.setEmployeeId(LoginForm.getAuthUser());
        log.setParameterId(chosenParameter);

        newProductTextField.setDisable(true);
        newProductBatchTextField.setDisable(true);
        newParameterTextField.setDisable(true);
        newParamMeasureTextField.setDisable(true);
        newReleaseFormTextField.setDisable(true);
        newProductMeasuretextField.setDisable(true);

        loadComboBoxData(ReleaseFormEntity.selectDistinctTitles(), releaseFormBox);
        loadComboBoxData(MeasurementUnitEntity.selectDistinctTitles(), unitMeasurementBox);
        loadComboBoxData(MeasurementUnitEntity.selectDistinctTitles(), boxParameterUnits);
        loadComboBoxData(ProductEntity.selectDistinctTitles(), productNameBox);
        loadComboBoxData(ParameterEntity.selectDistinctTitles(), parameterNameBox);

        //==================================Radio buttons configuration===================================
        newProductNameRadioButton.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            newProductTextField.setDisable(!isNowSelected);
            productNameBox.setDisable(isNowSelected);
            if(!isNowSelected)
                loadComboBoxData(ProductEntity.selectDistinctTitles(), productNameBox);
        });

        addListenerRadio(newProductBatchRadioButton, newProductBatchTextField, productBatchBox);
        addListenerRadio(newParamNameRadioButton, newParameterTextField, parameterNameBox);
        addListenerRadio(newReleaseFormRadioButton, newReleaseFormTextField, releaseFormBox);
        addListenerRadio(newProductMeasureRadioButton, newProductMeasuretextField, unitMeasurementBox);
        addListenerRadio(newParamMeasureRadioButton, newParamMeasureTextField, boxParameterUnits);

        //====================================Combo-box configuration======================================
        productNameBox.valueProperty().addListener((observableValue, o, t1) -> {
            chosenProduct.setTitle(t1);
            loadComboBoxData(chosenProduct.selectDistinctProductsBatches(), productBatchBox);
        });

        productBatchBox.valueProperty().addListener((observableValue, o, t1) -> {
            chosenProduct.setBatch(t1);
            loadComboBoxData(chosenProduct.selectDistinctReleaseForms(), releaseFormBox);
            loadComboBoxData(chosenProduct.selectDistinctMeasureUnits(), unitMeasurementBox);
        });

        releaseFormBox.valueProperty().addListener((observable, o, t1) -> {
            productReleaseForm.setTitle(t1);
            try {
                productReleaseForm.loadData();
                if (!chosenProduct.getTitle().isBlank() && !chosenProduct.getBatch().isBlank()) {
                    loadProductFields();
                }
            } catch (MyException e) {
                logger.error(e);
            }

        });

        unitMeasurementBox.valueProperty().addListener(((observableValue, s, t1) -> productMeasure.setTitle(t1)));

        productionDate.valueProperty().addListener(((observableValue, localDate, t1) ->
                chosenProduct.setManufactureDate(Date.valueOf(t1))));

        parameterNameBox.valueProperty().addListener((observable, o, t1) -> chosenParameter.setTitle(t1));

        boxParameterUnits.valueProperty().addListener(((observableValue, s, t1) -> parameterMeasure.setTitle(t1)));

        AsyncTask.create(() -> loadTableView(tableView)).run();
    }

     public InsertDataForm() {
        FXMLLoader loader = new FXMLLoader(MyApplication.class.getResource("fxml/InsertDataForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        } catch (Exception e) {
            logger.error("Error while loading FXML & set up controller InsertDataForm: " , e);
        }
    }

    private void loadProductFields() {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(chosenProduct.selectNameBatchFormJoin())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    batchSizeTextField.setText(String.valueOf(rs.getFloat("batch_size")));
                    unitMeasurementBox.setValue(rs.getString("title"));
                    productionDate.setValue(rs.getDate("manufacture_date").toLocalDate());
                }
            } catch (SQLException e) {
                logger.error("Can't execute query " + e);
            }
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
        }
    }

    private void doGoBack() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("TAA [trend analysis application]");
            stage.getIcons().add(new Image(MyApplication.class.getResourceAsStream("image/logo.png")));
            stage.setScene(new Scene(new MainMenuForm(), 600, 400));
            stage.show();
            InsertDataForm.this.getScene().getWindow().hide();
        });
    }

    private void doInput() {

        try {
            productMeasure.getInputData(productMeasureCombo);
            parameterMeasure.getInputData(parameterMeasureCombo);
            productReleaseForm.getInputData(releaseFormCombo);
            chosenProduct.getInputData(productCombo);
            chosenParameter.getInputData(parameterCombo);

            log.setValue(Float.valueOf(parameterValue.getText()));
            log.setAction("Ввод данных");
            log.makeInsert(log.insertStatement());
            log.loadData();
            addNewRowTableView();
        } catch (SQLException | MyException e){
            logger.error(e);
        }
    }

    private void addNewRowTableView() {

        ObservableList<String> row = FXCollections.observableArrayList();

        row.add(String.valueOf(log.getId()));
        row.add(chosenProduct.getTitle());
        row.add(chosenProduct.getBatch());
        row.add(productReleaseForm.getTitle());
        row.add(chosenParameter.getTitle());
        row.add(String.valueOf(log.getValue()));
        row.add(LoginForm.getAuthUser().getFio());
        row.add(String.valueOf(log.getTs()));

        tableView.getItems().add(row);
    }
}