package hr.algebra.theaterapp_gui.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record PlayXmlDto(

        @JacksonXmlProperty(isAttribute = true)
        String name,

        @JacksonXmlProperty(localName = "director")
        String director,

        @JacksonXmlElementWrapper(localName = "cast")
        @JacksonXmlProperty(localName = "actor")
        List<String> actors

) {
}