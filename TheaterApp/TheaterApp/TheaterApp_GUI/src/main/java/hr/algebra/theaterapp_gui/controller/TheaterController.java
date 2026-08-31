package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Theater;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.repository.TheaterRepository;
import hr.algebra.theaterapp_gui.TheaterApp;
import hr.algebra.theaterapp_gui.session.UserSession;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import hr.algebra.theaterapp_gui.util.SceneUtil;
import javafx.beans.property.ReadOnlyStringWrapper;
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

public class TheaterController implements Initializable {

    private static final Logger log =
            LoggerFactory.getLogger(TheaterController.class);

    private final TheaterRepository theaterRepository =
            RepositoryFactory.getTheaterRepository();

    private final ObservableList<Theater> theaters =
            FXCollections.observableArrayList();

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Theater> theaterTableView;

    @FXML
    private TableColumn<Theater, String> nameColumn;

    @FXML
    private TableColumn<Theater, String> cityColumn;

    @FXML
    private TableColumn<Theater, String> addressColumn;

    @FXML
    private TableColumn<Theater, Integer> foundedYearColumn;

    @FXML
    private TableColumn<Theater, Integer> capacityColumn;

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
        loadTheaters();

        log.info(
                "User '{}' opened the theaters view.",
                UserSession.getLoggedInUser().getUsername()
        );
    }

    private void configureTableColumns() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        cityColumn.setCellValueFactory(cellData -> {
            Theater theater = cellData.getValue();

            String cityName = theater.getCity() == null
                    ? ""
                    : theater.getCity().name();

            return new ReadOnlyStringWrapper(cityName);
        });

        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("address")
        );

        foundedYearColumn.setCellValueFactory(
                new PropertyValueFactory<>("foundedYear")
        );

        capacityColumn.setCellValueFactory(
                new PropertyValueFactory<>("auditoriumCapacity")
        );
    }

    private void configureTableData() {
        SortedList<Theater> sortedTheaters =
                new SortedList<>(theaters);

        sortedTheaters.comparatorProperty().bind(
                theaterTableView.comparatorProperty()
        );

        theaterTableView.setItems(sortedTheaters);
    }

    private void configureUserPermissions() {
        boolean isAdmin = UserSession.getLoggedInUser().isAdmin();

        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
    }

    private void loadTheaters() {
        theaters.setAll(
                theaterRepository.retrieveAll()
        );
    }

    @FXML
    private void search() {
        String searchText = searchField.getText()
                .trim()
                .toLowerCase();

        if (searchText.isBlank()) {
            theaterTableView.setItems(theaters);
            return;
        }

        List<Theater> searchResults = theaters.stream()
                .filter(theater -> {
                    String name = theater.getName() == null
                            ? ""
                            : theater.getName().toLowerCase();

                    return name.contains(searchText);

                })
                .toList();

        theaterTableView.setItems(
                FXCollections.observableArrayList(searchResults)
        );

        log.info(
                "User '{}' searched theaters with '{}'.",
                UserSession.getLoggedInUser().getUsername(),
                searchText
        );
    }

    @FXML
    private void addTheater() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        try {
            Stage ownerStage = (Stage) theaterTableView
                    .getScene()
                    .getWindow();

            SceneUtil.showModalWindow(
                    TheaterApp.class.getResource("/fxml/theater-form.fxml"), "Add theater", ownerStage
            );

            loadTheaters();

            log.info(
                    "Admin '{}' opened the add theater form.",
                    UserSession.getLoggedInUser().getUsername()
            );

        } catch (IOException e) {
            log.error("Unable to open the theater form.", e);
        }
    }

    @FXML
    private void showDetails() {
        Theater selectedTheater = getSelectedTheater();

        if (selectedTheater == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) theaterTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(TheaterApp.class.getResource("/fxml/theater-details.fxml"
            )
            );

            Parent root = loader.load();

            TheaterDetailsController detailsController = loader.getController();

            detailsController.setTheater(selectedTheater);

            SceneUtil.showModalWindow(root, "Theater details", ownerStage);

            log.info(
                    "User '{}' opened details for theater '{}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedTheater.getName()
            );

        } catch (IOException e) {
            log.error("Unable to open theater details.", e);

            AlertUtil.showError("Details error", "Unable to open theater details.");
        }
    }

    @FXML
    private void editTheater() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Theater selectedTheater = getSelectedTheater();

        if (selectedTheater == null) {
            return;
        }

        try {
            Stage ownerStage = (Stage) theaterTableView
                    .getScene()
                    .getWindow();

            FXMLLoader loader = new FXMLLoader(TheaterApp.class.getResource("/fxml/theater-form.fxml")
            );

            Parent root = loader.load();

            TheaterFormController formController = loader.getController();

            formController.setTheaterForEdit(selectedTheater);

            SceneUtil.showModalWindow(root, "Edit theater", ownerStage);

            loadTheaters();

            log.info(
                    "Admin '{}' opened edit form for theater '{}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedTheater.getName()
            );

        } catch (IOException e) {
            log.error("Unable to open theater edit form.", e);

        }
    }

    @FXML
    private void deleteTheater() {
        if (!UserSession.getLoggedInUser().isAdmin()) {
            showAccessDenied();
            return;
        }

        Theater selectedTheater = getSelectedTheater();

        if (selectedTheater == null) {
            return;
        }

        int deletedRows = theaterRepository.delete(
                selectedTheater.getId()
        );

        if (deletedRows > 0) {
            loadTheaters();

            log.info(
                    "Admin '{}' deleted theater '{}'.",
                    UserSession.getLoggedInUser().getUsername(),
                    selectedTheater.getName()
            );

            AlertUtil.showInformation(
                    "Theater deleted",
                    "The theater was successfully deleted."
            );
        } else {
            log.error(
                    "Theater '{}' could not be deleted.",
                    selectedTheater.getName()
            );

            AlertUtil.showError("Delete error", "The theater could not be deleted."
            );
        }
    }

    private Theater getSelectedTheater() {
        Theater selectedTheater = theaterTableView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedTheater == null) {
            AlertUtil.showWarning("No theater selected", "Select a theater from the table first."
            );
        }

        return selectedTheater;
    }

    private void showAccessDenied() {
        AlertUtil.showWarning("Access denied", "Only administrators can modify theater data."
        );

        log.warn(
                "User '{}' attempted an administrator action in theaters.",
                UserSession.getLoggedInUser().getUsername()
        );
    }
}