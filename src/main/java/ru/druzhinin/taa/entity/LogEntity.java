package ru.druzhinin.taa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.controller.LoginForm;
import ru.druzhinin.taa.enums.ErrorsCodes;
import ru.druzhinin.taa.utils.ConnectionUtil;
import ru.druzhinin.taa.utils.MyException;

import java.sql.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity implements DBentity{
    private int id;
    private String action;
    private ProductEntity productId;
    private ParameterEntity parameterId;
    private EmployeeEntity employeeId;
    private Float value;
    private java.sql.Timestamp ts;

    public LogEntity(String action, ProductEntity productId, ParameterEntity parameterId, EmployeeEntity employeeId, Float value) {
        this.action = action;
        this.productId = productId;
        this.parameterId = parameterId;
        this.employeeId = employeeId;
        this.value = value;
    }

    public LogEntity(String action, EmployeeEntity employeeId) {
        this.action = action;
        this.employeeId = employeeId;
    }

    public LogEntity(String action, ProductEntity productId, EmployeeEntity employeeId) {
        this.action = action;
        this.productId = productId;
        this.employeeId = employeeId;
    }

    public LogEntity(String action, ParameterEntity parameterId, EmployeeEntity employeeId) {
        this.action = action;
        this.parameterId = parameterId;
        this.employeeId = employeeId;
    }

    public LogEntity(ProductEntity productId, ParameterEntity parameterId, EmployeeEntity employeeId) {
        this.productId = productId;
        this.parameterId = parameterId;
        this.employeeId = employeeId;
    }

    @Override
    public String insertStatement() {
        Date utilDate = new Date();
        this.ts = new java.sql.Timestamp(utilDate.getTime());
        return "INSERT INTO polysan.log(action, date_time, product_id, parameter_id, employee_id, value) " +
                "VALUES ('" + this.action + "', '" + this.ts + "', " + this.productId.getId() + "," +
                this.parameterId.getId() + ", " + this.employeeId.getId() + ", " + this.getValue() + ");";
    }

    public String insertUserStatement() {
        Date utilDate = new Date();
        this.ts = new java.sql.Timestamp(utilDate.getTime());
        return "INSERT INTO polysan.log(action, date_time, product_id, parameter_id, employee_id, value) " +
                "VALUES ('" + this.action + "', '" + this.ts + "', null, null, " + this.employeeId.getId() + ", 0);";
    }

    public String insertProductStatement() {
        Date utilDate = new Date();
        this.ts = new java.sql.Timestamp(utilDate.getTime());
        return "INSERT INTO polysan.log(action, date_time, product_id, parameter_id, employee_id, value) " +
                "VALUES ('" + this.action + "', '" + this.ts + "', " + this.productId.getId() + ", null, " +
                this.employeeId.getId() + ", 0);";
    }

    public String insertParameterStatement() {
        Date utilDate = new Date();
        this.ts = new java.sql.Timestamp(utilDate.getTime());
        return "INSERT INTO polysan.log(action, date_time, product_id, parameter_id, employee_id, value) " +
                "VALUES ('" + this.action + "', '" + this.ts + "', null, " + this.parameterId.getId() + ", " +
                this.employeeId.getId() + ", 0);";
    }
    public String insertReleaseFormStatement() {
        Date utilDate = new Date();
        this.ts = new java.sql.Timestamp(utilDate.getTime());
        return "INSERT INTO polysan.log(action, date_time, product_id, parameter_id, employee_id, value) " +
                "VALUES ('" + this.action + "', '" + this.ts + "', " + this.productId.getId() + ", " +
                this.parameterId.getId() + ", " + this.employeeId.getId() + ", 0);";
    }


    @Override
    public String selectStatementID() {
        return "SELECT * FROM polysan.log WHERE log_id = '" + this.id + "';";
    }

    public static String selectMaxID() {
        return "SELECT MAX(log_id) FROM polysan.log;";
    }

    private static final Logger logger = LogManager.getLogger(LogEntity.class);

    @Override
    public void loadData() throws MyException{
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(selectMaxID())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    this.id = rs.getInt("MAX(log_id)");
                } else {
                    logger.debug("The table has no data with query, inserting query " + selectMaxID());
                    throw new MyException(ErrorsCodes.RECORD_NOT_FOUND);
                }
            } catch (SQLException e) {
                logger.error("Can't execute query " + e);
            }
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
        }
    }

    public void setTitle(String action) {

    }

    public static String selectStatementTableView () {
        return "select log.log_id as 'ID', product.name as 'Продукт', product.batch as 'Серия', release_form.title as 'Форма выпуска', " +
                "parameter.name as 'Параметр', log.value as 'Значение', employee.fio as 'ФИО', log.date_time as 'Дата/Время' " +
                "from polysan.log " +
                "join polysan.product on polysan.log.product_id = polysan.product.product_id " +
                "join polysan.release_form on polysan.product.release_form_id = polysan.release_form.release_form_id " +
                "join polysan.parameter on polysan.log.parameter_id = polysan.parameter.parameter_id " +
                "join polysan.employee on polysan.log.employee_id = polysan.employee.employee_id " +
                "where polysan.log.action != 'Авторизация';";
    }
}
