package hr.algebra.theaterapp_gui.controller;

import hr.algebra.exceptions.InvalidOibException;
import hr.algebra.model.Actor;
import hr.algebra.repository.ActorRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ActorFormController {

    private final ActorRepository actorRepository =
            RepositoryFactory.getActorRepository();
    private Actor actorForEdit;

    @FXML
    public Label formTitleLabel;

    @FXML
    private TextField actorIdField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField oibField;

    @FXML
    private void save() {
        try {
            Actor actor = createActorFromForm();

            if (actorForEdit == null) {
                actorRepository.create(actor);
            } else {
                actorRepository.update(actor);
            }

            closeWindow();

        } catch (InvalidOibException e) {
            AlertUtil.showError(
                    "Invalid OIB", "OIB must contain exactly 11 digits.");

        } catch (SQLException e) {
            e.printStackTrace();

            AlertUtil.showError("Database error", "The actor could not be saved.");

        } catch (Exception e) {
            e.printStackTrace();

            AlertUtil.showError("Error",
                    e.getMessage() == null
                            ? "An unexpected error occurred." : e.getMessage());
        }
    }

    private Actor createActorFromForm() throws InvalidOibException {
        String actorId = actorIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String oib = oibField.getText().trim();

        if (actorId.isBlank()) {
            throw new IllegalArgumentException("Actor ID is required.");
        }

        if (actorForEdit == null) {
            return new Actor(
                    firstName,
                    lastName,
                    oib,
                    actorId
            );
        }

        return new Actor(
                actorForEdit.getId(),
                firstName,
                lastName,
                oib,
                actorId
        );
    }
    public void setActorForEdit(Actor actor) {
        actorForEdit = actor;

        formTitleLabel.setText("Edit actor");

        actorIdField.setText(actor.getActorId());
        firstNameField.setText(actor.getFirstName());
        lastNameField.setText(actor.getLastName());
        oibField.setText(actor.getOib());
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