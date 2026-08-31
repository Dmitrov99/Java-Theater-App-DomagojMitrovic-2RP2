module hr.algebra.theaterapp_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires TheaterApp.model;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires org.slf4j;


    opens hr.algebra.theaterapp_gui to javafx.fxml;
    exports hr.algebra.theaterapp_gui;
    exports hr.algebra.theaterapp_gui.controller;
    exports hr.algebra.theaterapp_gui.task;
    exports hr.algebra.theaterapp_gui.util;
    exports hr.algebra.theaterapp_gui.dto.xml;

    exports hr.algebra.theaterapp_gui.service;
    opens hr.algebra.theaterapp_gui.controller to javafx.fxml;
}