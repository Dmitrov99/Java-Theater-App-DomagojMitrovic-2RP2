package hr.algebra.theaterapp_gui.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hr.algebra.theaterapp_gui.dto.xml.TheaterXmlDto;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TheaterXmlWriter {
    private TheaterXmlWriter() {
    }

    public static void write(TheaterXmlDto theaterXmlDto, Path outputPath) throws IOException {

        Path parentDirectory=outputPath.getParent();
        if(parentDirectory!=null){
            Files.createDirectories(parentDirectory);
        }
        XmlMapper xmlMapper=new XmlMapper();

        xmlMapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputPath.toFile(),theaterXmlDto);
    }
}
