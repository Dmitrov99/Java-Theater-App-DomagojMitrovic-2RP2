package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Director;
import hr.algebra.repository.DirectorRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.theaterapp_gui.TheaterApp;
import hr.algebra.theaterapp_gui.session.UserSession;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import hr.algebra.theaterapp_gui.util.SceneUtil;
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

public class DirectorController implements Initializable {

    private static final Logger log =
            LoggerFactory.getLogger(DirectorController.class);

    private final DirectorRepository directorRepository =
            RepositoryFactory.getDirectorRepository();

    private final ObservableList<Director> directors =
            FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Director> directorTableView;

    @FXML
    private TableColumn<Director, String> firstNameColumn;

    @FXML
    private TableColumn<Director, String> lastNameColumn;

    @FXML
    private TableColumn<Director, String> directionStyleColumn;

    @FXML
    private TableColumn<Director, String> directorIdColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        configureTableData();
        configureUserPermissions();
        loadDirectors();

        log.info(
                "User '{}' opened the directors view.",
                UserSession.getLoggedInUser().getUsername()
        );
    }

    private void loadDirectors() {
        directors.setAll(
                directorRepository.retrieveAll()
        );
    }

    private void configureTableData() {
        SortedList<Director> sortedDirectors =
                new SortedList<>(directors);

        sortedDirectors.comparatorProperty().bind(
                directorTableView.comparatorProperty()
        );

        directorTableView.setItems(sortedDirectors);
    }

    private void configureUserPermissions() {
        boolean isAdmin = UserSession.getLoggedInUser().isAdmin();

        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
    }

    private void configureTableColumns() {
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );

        directionStyleColumn.setCellValueFactory(
                new PropertyValueFactory<>("directionStyle")
        );

        directorIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("directorId")
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

        List<Director> searchResults = directors.stream()
                .filter(director -> {
                    String firstName = director.getFirstName() == null
                            ? ""
                            : director.getFirstName().toLowerCase();

                    String lastName = director.getLastName() == null
                            ? ""
                            : director.getLastName().toLowerCase();



                    return firstName.contains(searchText)
                            || lastName.contains(searchText);

                })
                .toList();

        directorTableView.setItems(
                FXCollections.observableArrayList(searchResults)
        );

        log.info(
                "User '{}' searched directors with '{}'.",
                UserSession.getLoggedInUser().getUsername(),
                searchText
        );
    }

    @FXML
    private void addDirector() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        try {
            Stage ownerStage = (Stage) directorTableView
                    .getScene()
                    .getWindow();

            SceneUtil.showModalWindow(
                    TheaterApp.class.getResource("/fxml/director-form.fxml"), "Add director", ownerStage);

            loadDirectors();

            log.info(
                    "Admin '{}' opened the add director form.",
                    UserSession.getLoggedInUser().getUsername()
            );

        } catch (IOException e) {
            log.error("Unable to open director form.", e);

            AlertUtil.showError("Form error", "Unable to open the director form.");
        }
    }

    @FXML
    private void editDirector() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Director selectedDirector = getSelectedDirector();

        if (selectedDirector == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) directorTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(TheaterApp.class.getResource("/fxml/director-form.fxml")
            );

            Parent root = loader.load();

            DirectorFormController formController = loader.getController();

            formController.setDirectorForEdit(selectedDirector);

            SceneUtil.showModalWindow(root, "Edit director", ownerStage);

            loadDirectors();

            log.info(
                    "Admin '{}' opened edit form for director '{} {}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedDirector.getFirstName(),
                    selectedDirector.getLastName()
            );

        } catch (IOException e) {
            log.error("Unable to open director edit form.", e);

        }
    }

    @FXML
    private void deleteDirector() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Director selectedDirector = getSelectedDirector();

        if (selectedDirector == null) {
            return;
        }

        int deletedRows = directorRepository.delete(
                selectedDirector.getId()
        );

        if (deletedRows > 0) {
            loadDirectors();

            log.info(
                    "Admin '{}' deleted director '{} {}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedDirector.getFirstName(),
                    selectedDirector.getLastName()
            );

            AlertUtil.showInformation("Director deleted", "The director was successfully deleted."
            );
        } else {
            log.error(
                    "Director '{} {}' could not be deleted.",
                    selectedDirector.getFirstName(),
                    selectedDirector.getLastName()
            );

            AlertUtil.showError("Delete error", "The director could not be deleted.");
        }
    }

    private Director getSelectedDirector() {
        Director selectedDirector = directorTableView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedDirector == null) {
            AlertUtil.showWarning("No director selected", "Select a director from the table first.");
        }

        return selectedDirector;
    }

    private void showAccessDenied() {
        AlertUtil.showWarning("Access denied", "Only administrators can modify director data.");

        log.warn("User '{}' attempted an administrator action in directors.", UserSession.getLoggedInUser().getUsername());
    }
}