package hr.algebra.repository;

import hr.algebra.model.Actor;
import hr.algebra.model.Play;


import java.util.List;

public interface PlayRepository extends EntityRepository<Play>{
    List<Play> findByName(String name);
    int performanceCounterIncrement(Long playId);
    int addActorToPlay(Long playId, Long actorId);
    int removeActorFromPlay(Long playId, Long actorId);
    List<Actor> findActorsByPlayId(Long playId);
    List<Play> findPlaysByTheaterId(Long theaterId);
}
