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
public class ReleaseFormEntity implements DBentity{
    private int id;
    private String title;

    @Override
    public String insertStatement() {
        return "INSERT INTO polysan.release_form(title) VALUES ('" + this.title +"');";
    }

    @Override
    public String selectStatementID() {
        return "SELECT * FROM polysan.release_form WHERE release_form_id = " + this.id +";";
    }

    public String selectStatementTitle() {
        return "SELECT release_form_id FROM polysan.release_form WHERE title = '" + this.title + "';";
    }

    public static String selectDistinctTitles() {
        return "SELECT title FROM polysan.release_form;";
    }

    private static final Logger logger = LogManager.getLogger(ReleaseFormEntity.class);

    public void loadData() throws MyException {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(this.selectStatementTitle())) {
                if(rs.isBeforeFirst()) {
                    rs.next();
                    this.id = rs.getInt("release_form_id");
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
