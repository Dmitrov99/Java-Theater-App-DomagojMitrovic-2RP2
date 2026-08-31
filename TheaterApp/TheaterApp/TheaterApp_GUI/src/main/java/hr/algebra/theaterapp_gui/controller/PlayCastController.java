package hr.algebra.theaterapp_gui.controller;

import hr.algebra.model.Actor;
import hr.algebra.model.Play;
import hr.algebra.repository.ActorRepository;
import hr.algebra.repository.PlayRepository;
import hr.algebra.repository.RepositoryFactory;
import hr.algebra.theaterapp_gui.util.AlertUtil;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PlayCastController {
    private final PlayRepository playRepository=RepositoryFactory.getPlayRepository();
    private final ActorRepository actorRepository=RepositoryFactory.getActorRepository();

    private Play play;

    @FXML
    private Label playNameLabel;

    @FXML
    private ListView<Actor> availableActorsListView;

    @FXML
    private ListView<Actor> castActorsListView;

    public void setPlay(Play play){
        this.play=play;
        playNameLabel.setText("Cast: "+play.getName());
        loadActors();
    }

    private void loadActors() {
        List<Actor> allActors=new ArrayList<>(actorRepository.retrieveAll());
        List<Actor> actorsInPlay=playRepository.findActorsByPlayId(play.getId());
        List<Actor> actorsNotInPlay=new ArrayList<>(allActors);


        actorsNotInPlay.removeAll(actorsInPlay);



        availableActorsListView.setItems(FXCollections.observableArrayList(actorsNotInPlay));
        castActorsListView.setItems(FXCollections.observableArrayList(actorsInPlay));
    }
    @FXML
    private void addActorToCast() {
        Actor selectedActor = availableActorsListView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedActor == null) {
            AlertUtil.showWarning("No actor selected", "Select an available actor first.");
            return;
        }

        int addedRows = playRepository.addActorToPlay(
                play.getId(),
                selectedActor.getId()
        );

        if (addedRows > 0) {
            loadActors();

            AlertUtil.showInformation("Actor added", "Actor was added to the cast.");
        } else {
            AlertUtil.showError("Add error", "Actor could not be added to the cast.");
        }
    }

    @FXML 
    private void removeActorFromCast(){
        Actor selectedActor=castActorsListView.getSelectionModel().getSelectedItem();
        if(selectedActor==null){
            AlertUtil.showWarning("Acotr not selected","Please select actor");
            return;
        }
        int removedCount=playRepository.removeActorFromPlay(play.getId(),selectedActor.getId());
        if(removedCount>0){
            loadActors();
            AlertUtil.showInformation("Actor removed","Actor successfully removed");
        }
        else AlertUtil.showError("Remove error","Couldn't remove actor");
    }

    @FXML
    private void close() {
        windowClose();
    }

    private void windowClose() {
        Stage stage = (Stage) playNameLabel
                .getScene()
                .getWindow();

        stage.close();
    }

    @FXML
    private void startDraggingFromActors(MouseEvent mouseEvent) {
        Actor selectedActor=availableActorsListView.getSelectionModel().getSelectedItem();
        if (selectedActor==null) return;
        ClipboardContent content=new ClipboardContent();

        content.putString(selectedActor.getId().toString());

        availableActorsListView
                .startDragAndDrop(TransferMode.MOVE)
                .setContent(content);

        mouseEvent.consume();
    }

    @FXML
    private void dropActorToCast(DragEvent dragEvent) {
        boolean success=false;
        if(dragEvent.getDragboard().hasString())
        {
            Long databaseId=Long.parseLong(dragEvent.getDragboard().getString());
            int addedRows=playRepository.addActorToPlay(play.getId(),databaseId);
            if(addedRows>0)
            {
                loadActors();
                success=true;
            }
            else
            {
                AlertUtil.showError("Add error","Actor could not be added");
            }
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    @FXML
    private void allowDropToCast(DragEvent dragEvent) {
        if(dragEvent.getGestureSource()!=castActorsListView && dragEvent.getDragboard().hasString())
        {
            dragEvent.acceptTransferModes(TransferMode.MOVE);
        }
        dragEvent.consume();
    }

    public void detectDraggingFromActors(MouseEvent mouseEvent) {
        mouseEvent.setDragDetect(true);
    }
    @FXML
    private void startDraggingFromCast(MouseEvent mouseEvent) {
        Actor selectedActor = castActorsListView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedActor == null) {
            return;
        }

        ClipboardContent content = new ClipboardContent();

        content.putString(
                selectedActor.getId().toString()
        );

        castActorsListView
                .startDragAndDrop(TransferMode.MOVE)
                .setContent(content);

        mouseEvent.consume();
    }
    @FXML
    private void detectDraggingFromCast(MouseEvent mouseEvent) {
        mouseEvent.setDragDetect(true);
    }
    @FXML
    private void allowDropToAvailable(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != availableActorsListView
                && dragEvent.getDragboard().hasString()) {

            dragEvent.acceptTransferModes(
                    TransferMode.MOVE
            );
        }

        dragEvent.consume();
    }
    @FXML
    private void dropActorToAvailable(DragEvent dragEvent) {
        boolean success = false;

        if (dragEvent.getDragboard().hasString()) {
            Long actorDatabaseId = Long.parseLong(
                    dragEvent.getDragboard().getString()
            );

            int removedRows = playRepository.removeActorFromPlay(
                    play.getId(),
                    actorDatabaseId
            );

            if (removedRows > 0) {
                loadActors();
                success = true;
            } else {
                AlertUtil.showError("Remove error", "Actor could not be removed from the cast.");
            }
        }

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}
