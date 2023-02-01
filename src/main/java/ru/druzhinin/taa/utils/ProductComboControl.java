package ru.druzhinin.taa.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductComboControl implements Combo {
    public String name;
    public RadioButton rb;
    public TextField tf;
    public ComboBox cb;
    public DatePicker dp;
    public TextField value;
    public ComboControl cc;
}
