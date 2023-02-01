module ru.druzhinin.taa {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires java.desktop;
    requires java.sql;
    requires lombok;
    requires org.testng;
    requires junit;


    opens ru.druzhinin.taa.fxml to javafx.fxml;
    exports ru.druzhinin.taa;
    exports ru.druzhinin.taa.controller;
    opens ru.druzhinin.taa.controller to javafx.fxml;
}