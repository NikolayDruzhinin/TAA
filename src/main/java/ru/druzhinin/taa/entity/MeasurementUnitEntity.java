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
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementUnitEntity implements DBentity{
    private int id;
    private String title;

    @Override
    public String insertStatement() {
        return "INSERT INTO polysan.measurement(title) VALUES ('" + this.title +"');";
    }

    @Override
    public String selectStatementID() {
        return "SELECT * FROM polysan.measurement WHERE measurement_id = " + this.id +";";
    }

    public String selectStatementTitle() {
        return "SELECT measurement_id FROM polysan.measurement WHERE title = '" + this.title + "';";
    }

    public static String selectDistinctTitles() {
        return "SELECT title FROM polysan.measurement;";
    }

    private static final Logger logger = LogManager.getLogger(MeasurementUnitEntity.class);

    public void loadData() throws MyException {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(this.selectStatementTitle())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    this.id = rs.getInt("measurement_id");
                } else {
                    logger.debug("The table has no data with query " + this.selectStatementTitle());
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
}
