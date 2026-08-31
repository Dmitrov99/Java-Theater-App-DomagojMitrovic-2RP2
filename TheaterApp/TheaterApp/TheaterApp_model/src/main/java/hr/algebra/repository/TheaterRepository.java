package hr.algebra.repository;


import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Theater;

import java.util.List;



public interface TheaterRepository extends EntityRepository<Theater>{
    List<Theater> findByName(String name);


    int addActorToTheater(Long theaterId, Long actorId);

    int removeActorFromTheater(Long theaterId, Long actorId);

    List<Actor> findActorsByTheaterId(Long theaterId);

    int addDirectorToTheater(Long theaterId, Long directorId);

    int removeDirectorFromTheater(Long theaterId, Long directorId);

    List<Director> findDirectorsByTheaterId(Long theaterId);
}
