package ru.druzhinin.taa.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.events.Event;
import ru.druzhinin.taa.MyApplication;
import ru.druzhinin.taa.entity.*;
import ru.druzhinin.taa.enums.ErrorsCodes;
import ru.druzhinin.taa.utils.DialogUtil;
import ru.druzhinin.taa.utils.MyException;

import java.net.URL;
import java.util.EventListener;
import java.util.ResourceBundle;

import static ru.druzhinin.taa.utils.ControlsUtil.loadComboBoxData;

public class GetStatisticsForm extends AnchorPane implements Initializable {

    @FXML
    private ComboBox<String> productName1;
    @FXML
    private ComboBox<String> fromBatch1;
    @FXML
    private ComboBox<String> toBatch1;
    @FXML
    private ComboBox<String> releaseForm1;
    @FXML
    private ComboBox<String> parameter1;
    @FXML
    private RadioButton secondGraphRB;
    @FXML
    private ComboBox<String> parameter2;
    @FXML
    private TextField lcl1;
    @FXML
    private TextField ucl1;
    @FXML
    private TextField lcl2;
    @FXML
    private TextField ucl2;
//    @FXML
//    private LineChart xChart;
    @FXML
    private LineChart mrChart;
    @FXML
    private Label sigmaL1;
    @FXML
    private Label sigmaT1;
    @FXML
    private Label sigmaL2;
    @FXML
    private Label sigmaT2;
    @FXML
    private Label cp1;
    @FXML
    private Label cpk1;
    @FXML
    private Label cp2;
    @FXML
    private Label cpk2;
    @FXML
    private Label pp1;
    @FXML
    private Label ppk1;
    @FXML
    private Label pp2;
    @FXML
    private Label ppk2;
    @FXML
    private Button makeGraphButton;
    @FXML
    private Button exportExcelButton;
    @FXML
    private CategoryAxis xAxisMRmap;
    @FXML
    private NumberAxis yAxisMRmap;
//    @FXML
//    private CategoryAxis xAxisXmap;
//    @FXML
//    private NumberAxis yAxisXmap;
    @FXML
    private Pane pane1;

    private ProductEntity product1;
    private ProductEntity product11;
    private ParameterEntity param1;
    private ParameterEntity param11;
    private ParameterEntity param2;
    private ParameterEntity param22;
    private ReleaseFormEntity releaseFormEntity1;
    private LogEntity log;

    @FXML
    protected void makeGraph() {
        makeGraphButton.setOnAction( e-> {
            checkControls();
            createGraph();
            exportExcelButton.setDisable(false);
        });
    }

    @FXML
    protected void goBack() {
        doGoBack();
    }

    @FXML
    protected void exportExcel() {

    }

    @AllArgsConstructor
    class Combo {
        ComboBox<String> parameterCombo;
        ParameterEntity parameter1;
        ParameterEntity parameter2;
        TextField lcl;
        TextField ucl;

        void setDisableFields(Boolean flag) {
            this.parameterCombo.setDisable(flag);
            this.lcl.setDisable(flag);
            this.ucl.setDisable(flag);
        }
    }

    Combo paramCombo1;
    Combo paramCombo2;

    private static final Logger logger = LogManager.getLogger(GetStatisticsForm.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        product1 = new ProductEntity();
        product11 = new ProductEntity();
        releaseFormEntity1 = new ReleaseFormEntity();
        product1.setReleaseForm(releaseFormEntity1);
        product11.setReleaseForm(releaseFormEntity1);

        param1 = new ParameterEntity();
        param1.setProductId(product1);
        param11 = new ParameterEntity();
        param11.setProductId(product11);
        param2 = new ParameterEntity();
        param2.setProductId(product1);
        param22 = new ParameterEntity();
        param22.setProductId(product11);

        paramCombo1 = new Combo(parameter1, param1, param11, lcl1, ucl1);
        paramCombo2 = new Combo(parameter2, param2, param22, lcl2, ucl2);

        log = new LogEntity(product1, param1, LoginForm.getAuthUser());

        paramCombo2.setDisableFields(true);
        makeGraphButton.setDisable(true);
        exportExcelButton.setDisable(true);

        loadComboBoxData(ReleaseFormEntity.selectDistinctTitles(), releaseForm1);
        loadComboBoxData(ProductEntity.selectDistinctTitles(), productName1);

        secondGraphRB.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            makeGraphButton.setDisable(isNowSelected);
            paramCombo2.setDisableFields(!isNowSelected);

            loadComboBoxData(param1.selectDistinctTitlesOnID(), parameter2);

            addParamListener(paramCombo2);
        });

        productName1.valueProperty().addListener((observableValue, o, t1) -> {
            product1.setTitle(t1);
            product11.setTitle(t1);
            loadComboBoxData(product1.selectDistinctProductsBatches(), fromBatch1);
            loadComboBoxData(product1.selectDistinctProductsBatches(), toBatch1);
        });

        fromBatch1.valueProperty().addListener((observableValue, o, t1) -> {
            product1.setBatch(t1);
        });

        toBatch1.valueProperty().addListener((observableValue, o, t1) -> {
            product11.setBatch(t1);
        });

        releaseForm1.valueProperty().addListener((observableValue, o, t1) -> {
            releaseFormEntity1.setTitle(t1);
            try {
                releaseFormEntity1.loadData();
                product1.loadData();
                product11.loadData();
                loadComboBoxData(param1.selectDistinctTitlesOnID(), parameter1);
            } catch (MyException e) {
                logger.error(e);
            }
        });

        addParamListener(paramCombo1);
    }

    void addParamListener(Combo c) {
        c.parameterCombo.valueProperty().addListener((observableValue, o, t1) -> {
            c.parameter1.setTitle(t1);
            c.parameter2.setTitle(t1);
            try {
                c.parameter1.loadData();
                c.parameter2.loadData();
                makeGraphButton.setDisable(false);
            } catch (MyException e) {
                logger.error(e);
            }
        });
    }

    private void createGraph() {
        drawXMap();
        drawMrMap();
    }

    private void drawMrMap() {
    }

    private void drawXMap() {
        pane1 = new Pane();
        var xAxis = new NumberAxis();
        xAxis.setLabel("Age");

        var yAxis = new NumberAxis();
        yAxis.setLabel("Salary (€)");

        var lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average salary per age");

        var data = new XYChart.Series<Number, Number>();
        data.setName("2016");

        data.getData().add(new XYChart.Data<>(18, 567));
        data.getData().add(new XYChart.Data<>(20, 612));
        data.getData().add(new XYChart.Data<>(25, 800));
        data.getData().add(new XYChart.Data<>(30, 980));
        data.getData().add(new XYChart.Data<>(40, 1410));
        data.getData().add(new XYChart.Data<>(50, 2350));

        lineChart.getData().add(data);

        pane1.getChildren().add(lineChart);
        Stage stage = new Stage();

        stage.setScene(GetStatisticsForm.this.getScene());
        stage.show();

    }



    private void checkControls() {
        try {
            checkInputCB(productName1);
            checkInputCB(fromBatch1);
            checkInputCB(toBatch1);
            checkInputCB(releaseForm1);
            checkInputCB(parameter1);

            getInputTF(lcl1);
            getInputTF(ucl1);
        } catch (MyException e) {
            logger.error(e);
        }

        if (secondGraphRB.isSelected()) {
            try {
                checkInputCB(parameter2);
                getInputTF(lcl2);
                getInputTF(ucl2);
            } catch (MyException e) {
                logger.error(e);
            }
        }
    }

    private void checkInputCB(ComboBox cb) throws MyException{
        if (cb.getSelectionModel().isEmpty()) {
            DialogUtil.showErrorLater("Выберите данные в поле \"" + cb.getPromptText() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cb.getClass().getName());
        }
    }

    private void getInputTF(TextField tf) throws MyException{
        if (tf.getText().isEmpty()) {
            DialogUtil.showErrorLater("Выберите данные в поле \"" + tf.getPromptText() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, tf.getClass().getName());
        }
    }

    public GetStatisticsForm() {
        FXMLLoader loader = new FXMLLoader(MyApplication.class.getResource("fxml/GetStatisticsForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        } catch (Exception e) {
            logger.error("Error while loading FXML & set up controller " + this.getClass().getName() , e);
        }
    }

    private void doGoBack() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("TAA [trend analysis application]");
            stage.getIcons().add(new Image(MyApplication.class.getResourceAsStream("image/logo.png")));
            stage.setScene(new Scene(new MainMenuForm(), 600, 400));
            stage.show();
            GetStatisticsForm.this.getScene().getWindow().hide();
        });
    }
}
