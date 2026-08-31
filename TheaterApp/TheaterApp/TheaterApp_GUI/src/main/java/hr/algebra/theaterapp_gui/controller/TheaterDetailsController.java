package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Play;
import hr.algebra.model.Theater;
import hr.algebra.repository.PlayRepository;
import hr.algebra.repository.RepositoryFactory;

import hr.algebra.theaterapp_gui.service.XmlExportService;
import hr.algebra.theaterapp_gui.util.AlertUtil;

import hr.algebra.utilities.DateUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TheaterDetailsController {


    private final PlayRepository playRepository =
            RepositoryFactory.getPlayRepository();
    XmlExportService exportService;
    private Theater theater;
    @FXML
    public ImageView theaterImageView;

    @FXML
    private Label theaterNameLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label foundedYearLabel;

    @FXML
    private Label capacityLabel;

    @FXML
    private TextArea historyTextArea;

    @FXML
    private TableView<Play> playTableView;

    @FXML
    private TableColumn<Play, String> playNameColumn;

    @FXML
    private TableColumn<Play, String> playTypeColumn;

    @FXML
    private TableColumn<Play, String> premierDateColumn;

    @FXML
    private TableColumn<Play, String> directorColumn;

    @FXML
    private TableColumn<Play, Integer> performanceCounterColumn;

    @FXML
    private void initialize() {
        configureTableColumns();
    }

    public void setTheater(Theater theater) {
        this.theater=theater;
        theaterNameLabel.setText(theater.getName());

        cityLabel.setText(
                theater.getCity() == null
                        ? ""
                        : theater.getCity().name()
        );

        addressLabel.setText(
                theater.getAddress() == null
                        ? ""
                        : theater.getAddress()
        );

        foundedYearLabel.setText(
                String.valueOf(theater.getFoundedYear())
        );

        capacityLabel.setText(
                String.valueOf(theater.getAuditoriumCapacity())
        );

        historyTextArea.setText(
                theater.getHistory() == null
                        ? ""
                        : theater.getHistory()
        );
        loadTheaterImage(theater.getImagePath());

        loadRepertoire(theater);
    }



    private void configureTableColumns() {
        playNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        playTypeColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String playType = play.getPlayType() == null
                    ? ""
                    : play.getPlayType().name();

            return new ReadOnlyStringWrapper(playType);
        });

        premierDateColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String formattedDate = play.getPremierDate() == null
                    ? ""
                    : DateUtils.formatCroatianDate(
                    play.getPremierDate()
            );

            return new ReadOnlyStringWrapper(formattedDate);
        });

        directorColumn.setCellValueFactory(cellData -> {
            Play play = cellData.getValue();

            String directorName = play.getDirector() == null
                    ? ""
                    : play.getDirector().toString();

            return new ReadOnlyStringWrapper(directorName);
        });

        performanceCounterColumn.setCellValueFactory(
                new PropertyValueFactory<>("performanceCounter")
        );
    }

    private void loadRepertoire(Theater theater) {
        playTableView.setItems(
                FXCollections.observableArrayList(
                        playRepository.findPlaysByTheaterId(
                                theater.getId()
                        )
                )
        );
    }

    private void loadTheaterImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            theaterImageView.setImage(null);
            return;
        }

        var imageUrl = getClass().getResource(imagePath);

        theaterImageView.setImage(
                new Image(imageUrl.toExternalForm())
        );
    }

    @FXML
    private void close() {
        Stage stage = (Stage) playTableView
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void exportXml() {
        Path outputPath = Path.of( "exports",theater.getName() + ".xml");

        exportService =
                new XmlExportService(
                        theater,
                        playRepository,
                        outputPath
                );

        exportService.setOnSucceeded(event -> {
            AlertUtil.showInformation("Export successful", "XML file created: " + exportService.getValue());
        });

        exportService.setOnFailed(event -> {
            AlertUtil.showError("Export error", "Unable to export theater data to XML.");

            exportService.getException().printStackTrace();
        });

        exportService.start();
    }
}
