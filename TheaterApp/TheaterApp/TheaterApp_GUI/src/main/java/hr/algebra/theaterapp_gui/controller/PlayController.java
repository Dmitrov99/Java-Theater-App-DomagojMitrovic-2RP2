package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Play;
import hr.algebra.repository.PlayRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.theaterapp_gui.TheaterApp;
import hr.algebra.theaterapp_gui.session.UserSession;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import hr.algebra.theaterapp_gui.util.SceneUtil;
import hr.algebra.utilities.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PlayController implements Initializable {

    private static final Logger log =
            LoggerFactory.getLogger(PlayController.class);

    private final PlayRepository playRepository =
            RepositoryFactory.getPlayRepository();

    private final ObservableList<Play> plays =
            FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Play> playTableView;

    @FXML
    private TableColumn<Play, String> nameColumn;

    @FXML
    private TableColumn<Play, String> directorColumn;

    @FXML
    private TableColumn<Play, String> theaterColumn;

    @FXML
    private TableColumn<Play, String> premierDateColumn;

    @FXML
    private TableColumn<Play, String> playTypeColumn;

    @FXML
    private TableColumn<Play, Integer> performanceCounterColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button castButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        configureTableData();
        configureUserPermissions();
        loadPlays();

        log.info(
                "User '{}' opened the plays view.",
                UserSession.getLoggedInUser().getUsername()
        );
    }

    private void loadPlays() {
        plays.setAll(
                playRepository.retrieveAll()
        );
    }

    private void configureTableData() {
        SortedList<Play> sortedPlays =
                new SortedList<>(plays);

        sortedPlays.comparatorProperty().bind(
                playTableView.comparatorProperty()
        );

        playTableView.setItems(sortedPlays);
    }

    private void configureUserPermissions() {
        boolean isAdmin = UserSession.getLoggedInUser().isAdmin();

        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
        castButton.setDisable(!isAdmin);
    }

    private void configureTableColumns() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        directorColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String directorName = play.getDirector() == null ? "" : play.getDirector().toString();

            return new SimpleStringProperty(directorName);
        });

        theaterColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String theaterName = play.getTheater() == null ? "" : play.getTheater().getName();

            return new SimpleStringProperty(theaterName);
        });

        premierDateColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String premierDate = play.getPremierDate() == null
                    ? ""
                    : DateUtils.formatCroatianDate(
                    play.getPremierDate()
            );

            return new SimpleStringProperty(premierDate);
        });

        playTypeColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String playType = play.getPlayType() == null
                    ? ""
                    : play.getPlayType().name();

            return new SimpleStringProperty(playType);
        });

        performanceCounterColumn.setCellValueFactory(
                new PropertyValueFactory<>("performanceCounter")
        );
    }

    @FXML
    private void search() {
        String searchText = searchField.getText()
                .trim()
                .toLowerCase();

        if (searchText.isBlank()) {
            configureTableData();
            return;
        }

        List<Play> searchResults = plays.stream()
                .filter(play -> {
                    String playName = play.getName() == null ? "" : play.getName().toLowerCase();

                    return playName.contains(searchText);
                })
                .toList();

        playTableView.setItems(FXCollections.observableArrayList(searchResults));

        log.info("User '{}' searched plays by name with '{}'.", UserSession.getLoggedInUser().getUsername(), searchText);
    }

    @FXML
    private void addPlay() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        try {
            Stage ownerStage = (Stage) playTableView
                    .getScene()
                    .getWindow();

            SceneUtil.showModalWindow(TheaterApp.class.getResource("/fxml/play-form.fxml"), "Add play", ownerStage);

            loadPlays();

            log.info("Admin '{}' opened the add play form.", UserSession.getLoggedInUser().getUsername()
            );

        } catch (IOException e) {
            log.error("Unable to open the play form.", e);

        }
    }

    @FXML
    private void editPlay() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Play selectedPlay = getSelectedPlay();

        if (selectedPlay == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) playTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(TheaterApp.class.getResource("/fxml/play-form.fxml")
            );

            Parent root = loader.load();

            PlayFormController formController = loader.getController();

            formController.setPlayForEdit(selectedPlay);

            SceneUtil.showModalWindow(root, "Edit play", ownerStage);

            loadPlays();

            log.info("Admin '{}' opened edit form for play '{}'.", UserSession.getLoggedInUser().getUsername(), selectedPlay.getName());

        } catch (IOException e) {
            log.error("Unable to open play edit form.", e);

            AlertUtil.showError("Form error", "Unable to open the play form.");
        }
    }

    @FXML
    private void deletePlay() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Play selectedPlay = getSelectedPlay();

        if (selectedPlay == null) {
            return;
        }

        int deletedRows = playRepository.delete(
                selectedPlay.getId()
        );

        if (deletedRows > 0) {
            loadPlays();

            log.info("Admin '{}' deleted play '{}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedPlay.getName()
            );

            AlertUtil.showInformation("Play deleted", "The play was successfully deleted.");
        } else {
            log.error("Play '{}' could not be deleted.", selectedPlay.getName());

            AlertUtil.showError("Delete error", "The play could not be deleted.");
        }
    }

    @FXML
    private void manageCast() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Play selectedPlay = getSelectedPlay();

        if (selectedPlay == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) playTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(
                    TheaterApp.class.getResource(
                            "/fxml/play-cast.fxml"
                    )
            );

            Parent root = loader.load();

            PlayCastController castController =
                    loader.getController();

            castController.setPlay(selectedPlay);

            SceneUtil.showModalWindow(root, "Manage cast", ownerStage
            );

            log.info(
                    "Admin '{}' opened cast management for play '{}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedPlay.getName()
            );

        } catch (IOException e) {
            log.error("Unable to open cast form.", e);

            AlertUtil.showError("Form error", "Unable to open cast form.");
        }
    }

    private Play getSelectedPlay() {
        Play selectedPlay = playTableView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedPlay == null) {
            AlertUtil.showWarning("No play selected", "Select a play from the table first.");
        }

        return selectedPlay;
    }

    private void showAccessDenied() {
        AlertUtil.showWarning("Access denied", "Only administrators can modify play data.");

        log.warn("User '{}' attempted an administrator action in plays.", UserSession.getLoggedInUser().getUsername());
    }
}