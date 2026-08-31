package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Actor;
import hr.algebra.repository.ActorRepository;
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

public class ActorController implements Initializable {

    private static final Logger log =
            LoggerFactory.getLogger(ActorController.class);

    private final ActorRepository actorRepository =
            RepositoryFactory.getActorRepository();

    private final ObservableList<Actor> actors =
            FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Actor> actorTableView;

    @FXML
    private TableColumn<Actor, String> firstNameColumn;

    @FXML
    private TableColumn<Actor, String> lastNameColumn;

    @FXML
    private TableColumn<Actor, String> actorIdColumn;

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
        loadActors();

        log.info(
                "User '{}' opened the actors view.",
                UserSession.getLoggedInUser().getUsername()
        );
    }

    private void loadActors() {
        actors.setAll(
                actorRepository.retrieveAll()
        );
    }

    private void configureTableData() {
        SortedList<Actor> sortedActors =
                new SortedList<>(actors);

        sortedActors.comparatorProperty().bind(
                actorTableView.comparatorProperty()
        );

        actorTableView.setItems(sortedActors);
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

        actorIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("actorId")
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

        List<Actor> searchResults = actors.stream()
                .filter(actor -> {
                    String firstName = actor.getFirstName() == null
                            ? ""
                            : actor.getFirstName().toLowerCase();

                    String lastName = actor.getLastName() == null
                            ? ""
                            : actor.getLastName().toLowerCase();

                    return firstName.contains(searchText)
                            || lastName.contains(searchText);
                })
                .toList();

        actorTableView.setItems(FXCollections.observableArrayList(searchResults));

        log.info(
                "User '{}' searched actors with '{}'.",
                UserSession.getLoggedInUser().getUsername(),
                searchText
        );
    }

    @FXML
    private void addActor() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;}


        try {
            Stage ownerStage = (Stage) actorTableView
                    .getScene()
                    .getWindow();

            SceneUtil.showModalWindow(
                    TheaterApp.class.getResource("/fxml/actor-form.fxml"), "Add actor", ownerStage);

            loadActors();

            log.info("Admin '{}' opened the add actor form.", UserSession.getLoggedInUser().getUsername());

        } catch (IOException e) {
            log.error("Unable to open actor form.", e);

        }
    }

    @FXML
    private void editActor() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Actor selectedActor = getSelectedActor();

        if (selectedActor == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) actorTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(TheaterApp.class.getResource("/fxml/actor-form.fxml"));

            Parent root = loader.load();

            ActorFormController formController = loader.getController();

            formController.setActorForEdit(selectedActor);

            SceneUtil.showModalWindow(root, "Edit actor", ownerStage);

            loadActors();

            log.info(
                    "Admin '{}' opened edit form for actor '{} {}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedActor.getFirstName(),
                    selectedActor.getLastName()
            );

        } catch (IOException e) {
            log.error("Unable to open actor edit form.", e);


        }
    }

    @FXML
    private void deleteActor() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Actor selectedActor = getSelectedActor();

        if (selectedActor == null) {
            return;
        }

        int deletedRows = actorRepository.delete(
                selectedActor.getId()
        );

        if (deletedRows > 0) {
            loadActors();

            log.info(
                    "Admin '{}' deleted actor '{} {}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedActor.getFirstName(),
                    selectedActor.getLastName()
            );

            AlertUtil.showInformation("Actor deleted", "The actor was successfully deleted.");
        } else {
            log.error("Actor '{} {}' could not be deleted.", selectedActor.getFirstName(), selectedActor.getLastName());


        }
    }

    private Actor getSelectedActor() {
        Actor selectedActor = actorTableView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedActor == null) {

        }

        return selectedActor;
    }

    private void showAccessDenied() {
        AlertUtil.showWarning("Access denied", "Only administrators can modify actor data.");

        log.warn("User '{}' attempted an administrator action in actors.", UserSession.getLoggedInUser().getUsername());
    }
}