package hr.algebra.theaterapp_gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class SceneUtil {

    private SceneUtil() {
    }

    public static void loadScene(URL fxmlUrl, Stage stage, String title) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
    }

    public static void showWindow(URL fxmlUrl, String title
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
    public static void showModalWindow(URL fxmlFormUrl, String title, Stage owner) throws IOException {

        FXMLLoader loader = new FXMLLoader(fxmlFormUrl);
        Stage formStage = new Stage();
        formStage.setTitle(title);
        formStage.setScene(new Scene(loader.load()));
        formStage.initOwner(owner);
        formStage.initModality(Modality.WINDOW_MODAL);
        formStage.showAndWait();
    }

    public static void showModalWindow(Parent root, String title, Stage owner){
        Stage formStage=new Stage();
        formStage.setTitle(title);
        formStage.setScene(new Scene(root));
        formStage.initOwner(owner);
        formStage.initModality(Modality.WINDOW_MODAL);
        formStage.showAndWait();
    }
}