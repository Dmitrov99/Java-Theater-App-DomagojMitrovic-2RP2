package hr.algebra.theaterapp_gui.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "theater")
public record TheaterXmlDto(

        @JacksonXmlProperty(isAttribute = true)
        String name,

        @JacksonXmlElementWrapper(localName = "seasons")
        @JacksonXmlProperty(localName = "season")
        List<SeasonXmlDto> seasons

) { }