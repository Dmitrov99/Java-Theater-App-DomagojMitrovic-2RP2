package hr.algebra.theaterapp_gui.util;

import hr.algebra.model.Actor;
import hr.algebra.model.Play;
import hr.algebra.model.Theater;
import hr.algebra.repository.PlayRepository;
import hr.algebra.theaterapp_gui.dto.xml.PlayXmlDto;
import hr.algebra.theaterapp_gui.dto.xml.SeasonXmlDto;
import hr.algebra.theaterapp_gui.dto.xml.TheaterXmlDto;
import hr.algebra.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class TheaterXmlMapper {
    private TheaterXmlMapper() {
    }

    public static TheaterXmlDto mapToDto(Theater theater,List<Play> plays, PlayRepository playRepository) {
        Map<String, List<Play>> playsBySeason = plays.stream()
                .collect(Collectors.groupingBy(
                        play -> DateUtils.calculateSeason(play.getPremierDate())
                ));
        Map<String, List<Play>> sortedPlaysBySeason = new TreeMap<>(playsBySeason);

        List<SeasonXmlDto> seasons = new ArrayList<>();

        sortedPlaysBySeason.forEach((seasonName, playsInSeason) ->
        {
            List<PlayXmlDto> playsDto = playsInSeason.stream()
                    .map(play -> mapPlayToDto(play, playRepository)).toList();
            seasons.add(new SeasonXmlDto(seasonName, playsDto));
        });
        return new TheaterXmlDto(theater.getName(), seasons);


    }

    private static PlayXmlDto mapPlayToDto(Play play, PlayRepository playRepository) {
        List<Actor> cast=playRepository.findActorsByPlayId(play.getId());
        List<String> actorNames=cast.stream().map(actor -> actor.toString()).toList();

        return new PlayXmlDto
            (play.getName(), play.
                    getDirector() == null ? "" : play.getDirector()
                    .toString(),
                    actorNames);


    };



}
