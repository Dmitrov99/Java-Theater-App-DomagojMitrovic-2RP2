package hr.algebra.theaterapp_gui.util;

import javafx.scene.control.Alert;

public final class AlertUtil {

    private AlertUtil() { }

    public static void showInformation(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
