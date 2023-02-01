package ru.druzhinin.taa.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.druzhinin.taa.controller.InsertDataForm;
import ru.druzhinin.taa.controller.LoginForm;

import java.sql.*;

import static ru.druzhinin.taa.entity.LogEntity.selectStatementTableView;

public class ControlsUtil {

    private static final Logger logger = LogManager.getLogger(InsertDataForm.class);

    public static void loadTableView(TableView tableView) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(selectStatementTableView())) {
                if(rs.isBeforeFirst()) {
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        final int j = i;
                        TableColumn col = new TableColumn(rs.getMetaData().getColumnLabel(i + 1));
                        col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                                new SimpleStringProperty(param.getValue().get(j).toString()));

                        tableView.getColumns().addAll(col);
                    }
                    while (rs.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            row.add(rs.getString(i));
                        }
                        data.add(row);
                    }
                    tableView.setItems(data);
                }
            } catch (SQLException e) {
                logger.error("Can't execute query " + e);
            }
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
        }
    }

    public static void loadComboBoxData(String query, ComboBox<String> cb) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                if(rs.isBeforeFirst()) {
                    String column = rs.getMetaData().getColumnName(1);
                    while(rs.next()) {
                        list.add(rs.getString(column));
                    }
                    cb.setItems(list);
                }
            } catch (SQLException e) {
                logger.error("Can't execute query " + e);
            }
        } catch (SQLException e) {
            logger.error("Connection to DB isn't established: " + e);
        }
    }
}
