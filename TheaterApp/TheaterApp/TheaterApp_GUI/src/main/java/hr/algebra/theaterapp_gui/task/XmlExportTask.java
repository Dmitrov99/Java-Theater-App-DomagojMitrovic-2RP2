package hr.algebra.theaterapp_gui.task;

import hr.algebra.model.Play;
import hr.algebra.model.Theater;
import hr.algebra.repository.PlayRepository;
import hr.algebra.theaterapp_gui.dto.xml.TheaterXmlDto;
import hr.algebra.theaterapp_gui.util.TheaterXmlMapper;
import hr.algebra.theaterapp_gui.util.TheaterXmlWriter;
import javafx.concurrent.Task;

import java.nio.file.Path;
import java.util.List;

public class XmlExportTask extends Task<Path> {
    private final Theater theater;
    private final PlayRepository playRepository;
    private final Path outputPath;



    public XmlExportTask(Theater theater, PlayRepository playRepository, Path outputPath) {

        this.theater = theater;
        this.playRepository = playRepository;
        this.outputPath = outputPath;

        }

    @Override
    protected Path call() throws Exception {
        List<Play> plays = playRepository.findPlaysByTheaterId(
                theater.getId()
        );

        TheaterXmlDto theaterXmlDto = TheaterXmlMapper.mapToDto(
                theater,
                plays,
                playRepository
        );

        TheaterXmlWriter.write(
                theaterXmlDto,
                outputPath
        );

        return outputPath;
    }

}
