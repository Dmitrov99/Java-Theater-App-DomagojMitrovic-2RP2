package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.DirectionStyle;
import hr.algebra.model.Director;
import hr.algebra.model.Play;
import hr.algebra.model.Theater;
import hr.algebra.repository.*;
import hr.algebra.theaterapp_gui.util.AlertUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PlayFormController implements Initializable {

    private final PlayRepository playRepository= RepositoryFactory.getPlayRepository();
    private final DirectorRepository directorRepository=RepositoryFactory.getDirectorRepository();
    private final ActorRepository actorRepository=RepositoryFactory.getActorRepository();
    private final TheaterRepository theaterRepository=RepositoryFactory.getTheaterRepository();
    private Play playForEdit;

    @FXML
    private Label formTitleLabel;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Director> directorComboBox;

    @FXML
    private ComboBox<Theater> theaterComboBox;

    @FXML
    private DatePicker premierDatePicker;

    @FXML
    private TextField performanceCounterField;

    @FXML
    private ComboBox<DirectionStyle> playTypeComboBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directorComboBox.getItems().setAll(directorRepository.retrieveAll());
        theaterComboBox.getItems().setAll(theaterRepository.retrieveAll());
        playTypeComboBox.getItems().setAll(DirectionStyle.values());
        performanceCounterField.setText("0");

    }

@FXML
    public void save() {
        try {
            Play play= createPlayFromForm();
            if (playForEdit==null) {
                playRepository.create(play);
                AlertUtil.showInformation("Play saved","Play successfully created");

            }
            else{
                Play updatedPlay=playForEdit.toBuilder()
                        .name(play.getName())
                        .playType(play.getPlayType())
                        .director(play.getDirector())
                        .premierDate(play.getPremierDate())
                        .theater(play.getTheater())
                        .performanceCounter(play.getPerformanceCounter())
                        .build();
                playRepository.update(updatedPlay);
            }
            closeWindow();
        }


        catch (NumberFormatException e) {
            AlertUtil.showError("Invalid number", "Performances must be a whole number.");

        } catch (SQLException e) {
            e.printStackTrace();

            AlertUtil.showError("Database error", "The play could not be saved.");

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




    private Play createPlayFromForm() {
        String name=nameField.getText().trim();

        Director director=directorComboBox.getValue();
        Theater theater=theaterComboBox.getValue();
        LocalDate premier=premierDatePicker.getValue();
        DirectionStyle playType=playTypeComboBox.getValue();

        int performanceCounter=Integer.parseInt(performanceCounterField.getText().trim());

        return Play.builder()
                .name(name)
                .director(director)
                .theater(theater)
                .premierDate(premier)
                .playType(playType)
                .performanceCounter(performanceCounter)
                .build();
    }
    @FXML
    public void cancel() {
      closeWindow();
    }
    private void closeWindow() {
        Stage stage=(Stage) nameField
                .getScene()
                .getWindow();
        stage.close();
    }

    public void setPlayForEdit(Play play) {
        playForEdit=play;
        formTitleLabel.setText("Edit play");
        nameField.setText(play.getName());
        directorComboBox.setValue(play.getDirector());
        theaterComboBox.setValue(play.getTheater());
        premierDatePicker.setValue(play.getPremierDate());
        performanceCounterField.setText(play.getPerformanceCounter().toString());
        playTypeComboBox.setValue(play.getPlayType());

    }
}
