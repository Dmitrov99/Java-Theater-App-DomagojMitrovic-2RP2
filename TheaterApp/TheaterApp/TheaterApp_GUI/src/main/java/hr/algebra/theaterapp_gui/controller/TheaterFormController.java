package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.City;
import hr.algebra.model.Country;
import hr.algebra.model.Theater;
import hr.algebra.repository.CityRepository;
import hr.algebra.repository.CountryRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.repository.TheaterRepository;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TheaterFormController {

    private final TheaterRepository theaterRepository =
            RepositoryFactory.getTheaterRepository();

    private final CityRepository cityRepository =
            RepositoryFactory.getCityRepository();

    private final CountryRepository countryRepository =
            RepositoryFactory.getCountryRepository();
    private Theater theaterForEdit;

    @FXML
    private Label formTitleLabel;
    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField foundedYearField;

    @FXML
    private TextField capacityField;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private ComboBox<City> cityComboBox;

    @FXML
    private TextField imagePathField;

    @FXML
    private TextArea historyTextArea;

    @FXML
    private void initialize() {
        countryComboBox.setItems(
                FXCollections.observableArrayList(
                        countryRepository.retrieveAll()
                )
        );

        cityComboBox.setItems(
                FXCollections.observableArrayList(
                        cityRepository.retrieveAll()
                )
        );
    }

    @FXML
    private void save() {
        try {
            Theater theater = createTheaterFromForm();

            if (theaterForEdit == null) {
                theaterRepository.create(theater);

                AlertUtil.showInformation(
                        "Theater saved",
                        "The theater was successfully added."
                );
            } else {
                Theater updatedTheater = theaterForEdit.toBuilder()
                        .name(theater.getName())
                        .address(theater.getAddress())
                        .foundedYear(theater.getFoundedYear())
                        .auditoriumCapacity(theater.getAuditoriumCapacity())
                        .history(theater.getHistory())
                        .imagePath(theater.getImagePath())
                        .country(theater.getCountry())
                        .city(theater.getCity())
                        .build();

                theaterRepository.update(updatedTheater);

                AlertUtil.showInformation(
                        "Theater updated",
                        "The theater was successfully updated."
                );
            }
        }
        catch (NumberFormatException e) {
            AlertUtil.showError(
                    "Invalid input",
                    "Founded year and auditorium capacity must be whole numbers."
            );

        } catch (IllegalArgumentException e) {
            AlertUtil.showError(
                    "Invalid input",
                    e.getMessage()
            );

        } catch (SQLException e) {
            AlertUtil.showError(
                    "Database error",
                    "The theater could not be saved."
            );
        }
    }

    private Theater createTheaterFromForm() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        int foundedYear = Integer.parseInt(
                foundedYearField.getText().trim()
        );
        int auditoriumCapacity = Integer.parseInt(
                capacityField.getText().trim()
        );
        String imagePath = imagePathField.getText().trim();
        String history = historyTextArea.getText().trim();

        Country country = countryComboBox.getValue();
        City city = cityComboBox.getValue();

        if (country == null) {
            throw new IllegalArgumentException(
                    "Please select a country."
            );
        }

        if (city == null) {
            throw new IllegalArgumentException(
                    "Please select a city."
            );
        }

        return Theater.builder()
                .name(name)
                .address(address)
                .foundedYear(foundedYear)
                .auditoriumCapacity(auditoriumCapacity)
                .history(history)
                .imagePath(imagePath)
                .country(country)
                .city(city)
                .build();
    }
    public void setTheaterForEdit(Theater theater) {
        theaterForEdit = theater;

        formTitleLabel.setText("Edit theater");

        nameField.setText(theater.getName());
        addressField.setText(theater.getAddress());

        foundedYearField.setText(
                String.valueOf(theater.getFoundedYear())
        );

        capacityField.setText(
                String.valueOf(theater.getAuditoriumCapacity())
        );

        historyTextArea.setText(theater.getHistory());
        imagePathField.setText(theater.getImagePath());

        countryComboBox.setValue(theater.getCountry());
        cityComboBox.setValue(theater.getCity());
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField
                .getScene()
                .getWindow();

        stage.close();
    }
}