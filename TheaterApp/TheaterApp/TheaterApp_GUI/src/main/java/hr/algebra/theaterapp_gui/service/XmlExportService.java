package hr.algebra.theaterapp_gui.service;

import hr.algebra.model.Play;
import hr.algebra.model.Theater;
import hr.algebra.repository.PlayRepository;
import hr.algebra.theaterapp_gui.task.XmlExportTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.nio.file.Path;

public class XmlExportService extends Service<Path> {

    private final Theater theater;
    private final PlayRepository playRepository;
    private final Path outputPath;

    public XmlExportService(Theater theater, PlayRepository playRepository, Path outputPath) {
        this.theater = theater;
        this.playRepository = playRepository;
        this.outputPath = outputPath;
    }

    @Override
    protected Task<Path> createTask() {
        return new XmlExportTask(theater,playRepository,outputPath);
    }
}
