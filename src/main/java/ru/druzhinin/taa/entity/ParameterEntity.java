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
public class ParameterEntity implements DBentity{
    private int id;
    private String title;
    private MeasurementUnitEntity parameterCountUnit;
    private ProductEntity productId;

    @Override
    public String insertStatement() {
        return "INSERT INTO polysan.parameter(name, param_measurement_id, product_id) " +
                "VALUES ('" + this.title + "', " + this.parameterCountUnit.getId() + ", " + this.productId.getId() + ");";
    }

    public String selectStatementID() {
        return "SELECT * FROM polysan.parameter WHERE parameter_id = " + this.id + ";";
    }

    public String selectStatementTitle() {
        return "SELECT * FROM polysan.parameter WHERE name = '" + this.title + "';";
    }

    public String selectDistinctTitlesOnID() {
        return "SELECT DISTINCT name FROM polysan.parameter WHERE product_id = " + this.productId.getId() + ";";
    }

    public static String selectDistinctTitles() {
        return "SELECT DISTINCT name FROM polysan.parameter;";
    }

    public String selectStatementTitleID() {
        return "SELECT * FROM polysan.parameter WHERE name = '" + this.title + "' and product_id = " + this.productId.getId() + ";";
    }

    private static final Logger logger = LogManager.getLogger(ParameterEntity.class);

    public void loadData() throws MyException {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(this.selectStatementTitleID())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    this.id = rs.getInt("parameter_id");
                } else {
                    logger.debug("The table has no data with query " + this.selectStatementTitleID());
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
    public void getInputData(Combo cc) throws SQLException, MyException {
        if (cc.getRb().isSelected() && cc.getTf().getText().trim().isEmpty()) {
            DialogUtil.showErrorLater("Введите данные в поле \"" + cc.getName() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getTf().getClass().getName());
        } else if (!cc.getRb().isSelected() && cc.getCb().getSelectionModel().isEmpty()) {
            DialogUtil.showErrorLater("Выберите данные в поле \"" + cc.getName() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cc.getCb().getClass().getName());
        } else if (cc.getValue().getText().isEmpty()) {
            DialogUtil.showErrorLater("Введите значение параметра");
            throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getValue().getClass().getName());
        }else {
            if (cc.getRb().isSelected())
                this.setTitle(cc.getTf().getText().trim());
            try {
                this.loadData();
            } catch (MyException e) {
                this.makeInsert(this.insertStatement());
                this.loadData();
                LogEntity le = new LogEntity("Добавление параметра", this, LoginForm.getAuthUser());
                le.makeInsert(le.insertParameterStatement());
            }
        }
    }
}
