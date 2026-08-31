package hr.algebra.theaterapp_gui.controller;

import hr.algebra.exceptions.InvalidOibException;
import hr.algebra.model.DirectionStyle;
import hr.algebra.model.Director;
import hr.algebra.repository.DirectorRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DirectorFormController implements Initializable {

    private final DirectorRepository directorRepository =
            RepositoryFactory.getDirectorRepository();

    private Director directorForEdit;

    @FXML
    public Label formTitleLabel;

    @FXML
    private TextField directorIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField oibField;

    @FXML
    private ComboBox<DirectionStyle> directionStyleComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directionStyleComboBox.getItems()
                .addAll(DirectionStyle.values());
    }

    public void setDirectorForEdit(Director director) {
        directorForEdit = director;

        formTitleLabel.setText("Edit director");

        directorIdField.setText(director.getDirectorId());
        firstNameField.setText(director.getFirstName());
        lastNameField.setText(director.getLastName());
        oibField.setText(director.getOib());

        directionStyleComboBox.setValue(
                director.getDirectionStyle()
        );
    }

    @FXML
    private void save() {
        try {
            Director director = createDirectorFromForm();

            if (directorForEdit == null) {
                directorRepository.create(director);
            } else {
                directorRepository.update(director);
            }

            closeWindow();

        } catch (InvalidOibException e)
        {AlertUtil.showError("Invalid OIB", "OIB must contain exactly 11 digits.");

        } catch (SQLException e) {
            e.printStackTrace();

            AlertUtil.showError(
                    "Database error",
                    "The director could not be saved."
            );

        } catch (Exception e) {
            e.printStackTrace();

            AlertUtil.showError(
                    "Error",
                    e.getMessage() == null
                            ? "An unexpected error occurred."
                            : e.getMessage()
            );
        }
    }

    private Director createDirectorFromForm() throws InvalidOibException {
        String directorId = directorIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String oib = oibField.getText().trim();

        DirectionStyle directionStyle =
                directionStyleComboBox.getValue();

        if (directorId.isBlank()) {
            throw new IllegalArgumentException(
                    "Director ID is required."
            );
        }

        if (directionStyle == null) {
            throw new IllegalArgumentException(
                    "Direction style is required."
            );
        }

        if (directorForEdit == null) {
            return new Director(
                    firstName,
                    lastName,
                    oib,
                    directionStyle,
                    directorId
            );
        }

        return new Director(
                directorForEdit.getId(),
                firstName,
                lastName,
                oib,
                directionStyle,
                directorId
        );
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField
                .getScene()
                .getWindow();

        stage.close();
    }
}