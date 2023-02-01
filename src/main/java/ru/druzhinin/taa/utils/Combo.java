package ru.druzhinin.taa.utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public interface Combo {
    String getName();
    RadioButton getRb();
    TextField getTf();
    ComboBox getCb();
    ComboControl getCc();
    TextField getValue();
    DatePicker getDp();
}
