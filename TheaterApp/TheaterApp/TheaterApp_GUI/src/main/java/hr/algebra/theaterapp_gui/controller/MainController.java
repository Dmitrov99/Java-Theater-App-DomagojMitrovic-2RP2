package hr.algebra.theaterapp_gui.controller;

import hr.algebra.theaterapp_gui.TheaterApp;
import hr.algebra.theaterapp_gui.util.SceneUtil;
import hr.algebra.theaterapp_gui.util.ViewUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private void showTheaters() {
       ViewUtil.loadView(TheaterApp.class.getResource("/fxml/theater.fxml"),mainBorderPane);
    }

    @FXML
    private void showPlays() {
        ViewUtil.loadView(TheaterApp.class.getResource("/fxml/play.fxml"),mainBorderPane);
    }

    @FXML
    private void showActors() {
        ViewUtil.loadView(TheaterApp.class.getResource("/fxml/actor.fxml"),mainBorderPane);
    }

    @FXML
    private void showDirectors() {
        ViewUtil.loadView(TheaterApp.class.getResource("/fxml/director.fxml"),mainBorderPane);
    }


    @FXML
    private void loadInitialData() {
        System.out.println("Loading initial data...");
    }

    @FXML
    private void deleteAllData() {
        System.out.println("Deleting all application data...");
    }

    @FXML
    private void logout() throws IOException {
        Stage stage=(Stage) mainBorderPane.getScene().getWindow();
        SceneUtil.loadScene(TheaterApp.class.getResource("/fxml/login.fxml"),stage,"Title");
    }

    @FXML
    private void exit() {
        Platform.exit();
    }
}