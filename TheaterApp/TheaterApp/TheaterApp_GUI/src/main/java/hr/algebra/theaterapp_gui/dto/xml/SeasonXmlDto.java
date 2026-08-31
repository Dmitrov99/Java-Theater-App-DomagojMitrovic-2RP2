package hr.algebra.theaterapp_gui.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record SeasonXmlDto(

        @JacksonXmlProperty(isAttribute = true)
        String name,

        @JacksonXmlElementWrapper(localName = "plays")
        @JacksonXmlProperty(localName = "play")
        List<PlayXmlDto> plays

) {
}