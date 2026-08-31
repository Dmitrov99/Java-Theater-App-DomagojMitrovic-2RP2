package hr.algebra.theaterapp_gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

//todo logiranje i custom exceptions
public final class ViewUtil {
    private ViewUtil() {
    }

    public static void loadView(URL fxmlUrl, BorderPane mainBorderPane) {

        if(fxmlUrl==null){
            AlertUtil.showError("Error loading fxml","file not found");
        }
        try {
            Node view= FXMLLoader.load(Objects.requireNonNull(fxmlUrl));
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            AlertUtil.showError("Error loading fxml","Resource could not be found");
            throw new RuntimeException(e);
        }
    }

}
