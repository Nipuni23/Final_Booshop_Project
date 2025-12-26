module lk.ijse.bookshop1{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires net.sf.jasperreports.core;
    requires javafx.base;
    requires jdk.compiler;

    opens lk.ijse.bookshop1.controller to javafx.fxml;
    opens lk.ijse.bookshop1.dto to java.base;
    exports lk.ijse.bookshop1;
    exports lk.ijse.bookshop1.controller;
    exports lk.ijse.bookshop1.dto;
    
}
