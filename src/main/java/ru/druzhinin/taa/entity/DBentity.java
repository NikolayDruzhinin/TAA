package ru.druzhinin.taa.entity;


import ru.druzhinin.taa.enums.ErrorsCodes;
import ru.druzhinin.taa.utils.*;
import ru.druzhinin.taa.controller.LoginForm;

import java.sql.*;

public interface DBentity {
    String insertStatement();
    String selectStatementID();

    void loadData() throws MyException, SQLException;
    void setTitle(String title);

    default void getInputData(Combo cc) throws SQLException, MyException {
        if (cc.getRb().isSelected() && cc.getTf().getText().trim().isEmpty()) {
            DialogUtil.showErrorLater("Введите данные в поле \"" + cc.getName() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_TEXT_FIELD, cc.getTf().getClass().getName());
        } else if (!cc.getRb().isSelected() && cc.getCb().getSelectionModel().isEmpty()) {
            DialogUtil.showErrorLater("Выберите данные в поле \"" + cc.getName() + "\"");
            throw new MyException(ErrorsCodes.EMPTY_COMBOBOX, cc.getCb().getClass().getName());
        } else {
            if (cc.getRb().isSelected())
                this.setTitle(cc.getTf().getText().trim());
            try {
                this.loadData();
            } catch (MyException e) {
                this.makeInsert(this.insertStatement());
                this.loadData();
                LogEntity le = new LogEntity("Добавление eд.измерения: " + this, LoginForm.getAuthUser());
                le.makeInsert(le.insertUserStatement());
            }
        }
    }

    default void makeInsert(String query) throws SQLException, MyException{
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.getURL(),
                LoginForm.getAuthUser().getLogin(), LoginForm.getAuthUser().getPassword());
             Statement stmt = conn.createStatement()) {
            int insertResult = stmt.executeUpdate(query);
            if (insertResult < 0)
                throw new MyException(ErrorsCodes.ERROR_UPDATE_TABLE, query);
        }
    }

}
