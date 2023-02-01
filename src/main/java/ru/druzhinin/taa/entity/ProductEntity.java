package ru.druzhinin.taa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.controller.LoginForm;
import ru.druzhinin.taa.enums.ErrorsCodes;
import ru.druzhinin.taa.utils.*;

import java.sql.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity implements DBentity{
    private int id;
    private String title;
    private String batch;
    private java.sql.Date manufactureDate;
    private Float batchSize;
    private ReleaseFormEntity releaseForm;
    private MeasurementUnitEntity productCountUnit;

    @Override
    public String insertStatement() {
        return "INSERT INTO polysan.product(name, batch, manufacture_date, batch_size, prod_measurement_id, release_form_id) " +
                "VALUES ('" + this.title + "', '" + this.batch + "', '" + this.manufactureDate + "','" +
                this.batchSize + "', '" + this.productCountUnit.getId() + "', '" + this.releaseForm.getId() + "');";
    }

    @Override
    public String selectStatementID() {
        return "SELECT * FROM polysan.product WHERE product_id = '" + this.id + "';";
    }

    public static String selectDistinctTitles() {
        return "SELECT DISTINCT name FROM polysan.product;";
    }

    public String selectDistinctProductsBatches() {
        return "SELECT DISTINCT batch FROM polysan.product WHERE name = '" + this.title + "';" ;
    }

    public String selectDistinctReleaseForms() {
        return "SELECT DISTINCT release_form.title FROM polysan.release_form " +
                "JOIN polysan.product ON polysan.product.release_form_id = polysan.release_form.release_form_id;";
    }

    public String selectDistinctMeasureUnits() {
        return "SELECT measurement.title FROM polysan.measurement " +
                "JOIN polysan.product ON polysan.product.prod_measurement_id = polysan.measurement.measurement_id;";
    }

    public String selectNameBatchForm() {
       return "SELECT * FROM polysan.product WHERE name = '" + this.title + "' and batch = '" + this.batch +
                "' and release_form_id = " + this.releaseForm.getId() + ";" ;
    }

    public String selectNameBatchFormJoin() {
        return "SELECT manufacture_date, batch_size, measurement.title  " +
                "FROM polysan.product " +
                "JOIN polysan.measurement ON prod_measurement_id = measurement_id " +
                "WHERE name = '" + this.title + "' and batch = '" + this.batch +
                "' and release_form_id = " + this.releaseForm.getId() + ";" ;
    }

    private static final Logger logger = LogManager.getLogger(ProductEntity.class);

    public void loadData() throws MyException {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(this.selectNameBatchForm())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    this.id = rs.getInt("product_id");
                    this.manufactureDate = rs.getDate("manufacture_date");
                    this.batchSize = rs.getFloat("batch_size");
                } else {
                    logger.debug("The table has no data with query, inserting query " + this.selectNameBatchForm());
                    throw new MyException(ErrorsCodes.RECORD_NOT_FOUND);
                }
            } catch (SQLException e) {
                logger.error("Can't execute query " + e);
            }
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
        }
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void getInputData(Combo cc) throws SQLException, MyException {
        if (cc.getRb().isSelected()) {
            if (cc.getTf().getText().trim().isEmpty()) {
                DialogUtil.showErrorLater("Введите наименование препарата");
                throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getTf().getClass().getName());
            } else
                this.setTitle(cc.getTf().getText().trim());
        } else {
            if (cc.getCb().getSelectionModel().isEmpty()) {
                DialogUtil.showErrorLater("Выберите наименование препарата");
                throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cc.getCb().getClass().getName());
            }
        }

        if (cc.getCc().getRb().isSelected()) {
            if (cc.getCc().getTf().getText().trim().isEmpty()) {
                DialogUtil.showErrorLater("Введите серию препарата");
                throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getCc().getTf().getClass().getName());
            } else
                this.setBatch(cc.getCc().getTf().getText().trim());
        } else
            if (cc.getCc().getCb().getSelectionModel().isEmpty()) {
                DialogUtil.showErrorLater("Выберите серию препарата");
                throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cc.getCc().getCb().getClass().getName());
            }
        if (cc.getValue().getText().trim().isEmpty()) {
            DialogUtil.showErrorLater("Введите объем серии препарата");
            throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getValue().getClass().getName());
        } else
            this.setBatchSize(Float.valueOf(cc.getValue().getText().trim()));

        if (cc.getDp().getValue() == null) {
            DialogUtil.showErrorLater("Выберите дату производства");
            throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cc.getDp().getClass().getName());
        } else
            this.setManufactureDate(Date.valueOf(cc.getDp().getValue()));

        try {
            this.loadData();
        } catch (MyException e) {
            this.makeInsert(this.insertStatement());
            this.loadData();
            LogEntity le = new LogEntity("Добавление продукта", this, LoginForm.getAuthUser());
            le.makeInsert(le.insertProductStatement());
        }
    }
}
