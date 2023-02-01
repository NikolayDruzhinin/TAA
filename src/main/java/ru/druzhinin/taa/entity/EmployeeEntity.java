package ru.druzhinin.taa.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.enums.ErrorsCodes;
import ru.druzhinin.taa.utils.ComboControl;
import ru.druzhinin.taa.utils.ConnectionUtil;
import ru.druzhinin.taa.utils.DialogUtil;
import ru.druzhinin.taa.utils.MyException;

import java.sql.*;

/**
 * класс сущность клиентского пользователя
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity implements DBentity {
    private int id;
    private String fio;
    private String position;
    private String login;
    private String password;

    @Override
    public String insertStatement() {
        return "INSERT INTO polysan.employee " +
                "VALUES (" + this.fio + ", " + this.position + ", " + this.login + ");";
    }

    @Override
    public String selectStatementID() {
        return "SELECT * FROM polysan.employee WHERE employee_id = '" + this.id + "';";
    }

    public String selectStatementLogin() {
        return "SELECT * FROM polysan.employee WHERE login = '" + this.login + "';";
    }

    public void setTitle(String title) {

    }

    private static final Logger logger = LogManager.getLogger(EmployeeEntity.class);

    @Override
    public void loadData() throws MyException, SQLException{
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(), this.login, this.password);
             Statement stmt = conn.createStatement()){
            try (ResultSet rs = stmt.executeQuery(this.selectStatementLogin())) {
                if(!rs.isBeforeFirst())  {
                    logger.debug("The table has no data with query " + this.selectStatementLogin());
                    throw new MyException(ErrorsCodes.RECORD_NOT_FOUND);
                } else {
                    rs.next();
                    this.id = rs.getInt("employee_id");
                    this.fio = rs.getString("fio");
                    this.position = rs.getString("position");
                }
            } catch (SQLException e) {
                logger.error("Can't execute statement: " + e);
            }
        }
    }
}
