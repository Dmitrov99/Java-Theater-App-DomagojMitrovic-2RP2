package hr.algebra.repository.sql;

import hr.algebra.model.*;
import hr.algebra.repository.TheaterRepository;
import hr.algebra.repository.sql.mapper.ActorRowMapper;
import hr.algebra.repository.sql.mapper.DirectorMapper;

import hr.algebra.repository.sql.mapper.TheaterRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static hr.algebra.repository.sql.queries.TheaterSqlQueries.*;

//todo builder za sve klase, SQL_SELECT_BASE za sve klase smanji boilerplate
public class TheaterSqlRepository extends BaseSqlRepository<Theater> implements TheaterRepository {

    private static final Logger log= LoggerFactory.getLogger(TheaterSqlRepository.class);

    public TheaterSqlRepository(DataSource dataSource) {
        super(dataSource);
    }



    @Override
    public List<Theater> findByName(String name) {
        try {
            return findMany(
                    SQL_FIND_BY_NAME,
                    ps -> ps.setString(1, "%" + name.trim() + "%")
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to find theaters matching name '{}'.",
                    name,
                    e
            );
            return List.of();
        }
    }



    @Override
    public Theater create(Theater theater) throws SQLException {
        long id = executeInsertReturningId(
                SQL_INSERT,
                ps -> mapEntityToTable(ps, theater)
        );

        return theater.toBuilder()
                .id(id)
                .build();
    }

    @Override
    public Optional<Theater> findById(Long id) {
        try {
            return findSingleResult(
                    SQL_FIND_BY_ID,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to find theater with ID={}.", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Theater> retrieveAll() {
        try {
            return findMany(
                    SQL_FIND_ALL,
                    ps -> {}
            );

        } catch (SQLException e) {
            log.error("Failed to retrieve all theaters.", e);
            return List.of();
        }
    }

    @Override
    public int update(Theater theater) {
        try {
            return executeUpdate(
                    SQL_UPDATE,
                    ps -> {
                        mapEntityToTable(ps, theater);
                        ps.setLong(9, theater.getId());
                    }
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to update theater with ID={}.",
                    theater.getId(),
                    e
            );
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        try {
            return executeUpdate(
                    SQL_DELETE,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to delete theater with ID={}.", id, e);
            return 0;
        }
    }


    @Override
    public int addActorToTheater(Long theaterId, Long actorId) {
        try {
            return executeUpdate(
                    ADD_ACTOR,
                    ps -> {
                        ps.setLong(1, theaterId);
                        ps.setLong(2, actorId);
                    }
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to add actor ID={} to theater ID={}.",
                    actorId,
                    theaterId,
                    e
            );
            return 0;
        }
    }

    @Override
    public int removeActorFromTheater(Long theaterId, Long actorId) {
        try {
            return executeUpdate(
                    REMOVE_ACTOR,
                    ps -> {
                        ps.setLong(1, theaterId);
                        ps.setLong(2, actorId);
                    }
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to remove actor ID={} from theater ID={}.",
                    actorId,
                    theaterId,
                    e
            );
            return 0;
        }
    }

    @Override
    public List<Actor> findActorsByTheaterId(Long theaterId) {
        try {
            return findMany(
                    FIND_ACTORS_BY_THEATER_ID,
                    ps -> ps.setLong(1, theaterId),
                    ActorRowMapper.ACTOR_ROW_MAPPER
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to find actors for theater ID={}.",
                    theaterId,
                    e
            );
            return List.of();
        }
    }

    @Override
    public int addDirectorToTheater(Long theaterId, Long directorId) {
        try {
            return executeUpdate(
                    ADD_DIRECTOR,
                    ps -> {
                        ps.setLong(1, theaterId);
                        ps.setLong(2, directorId);
                    }
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to add director ID={} to theater ID={}.",
                    directorId,
                    theaterId,
                    e
            );
            return 0;
        }
    }

    @Override
    public int removeDirectorFromTheater(Long theaterId, Long directorId) {
        try {
            return executeUpdate(
                    REMOVE_DIRECTOR,
                    ps -> {
                        ps.setLong(1, theaterId);
                        ps.setLong(2, directorId);
                    }
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to remove director ID={} from theater ID={}.",
                    directorId,
                    theaterId,
                    e
            );
            return 0;
        }
    }

    @Override
    public List<Director> findDirectorsByTheaterId(Long theaterId) {
        try {
            return findMany(
                    FIND_DIRECTORS_BY_THEATER_ID,
                    ps -> ps.setLong(1, theaterId),
                    DirectorMapper.DIRECTOR_MAPPER
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to find directors for theater ID={}.",
                    theaterId,
                    e
            );
            return List.of();
        }
    }


    @Override
        protected Theater mapRow(ResultSet rs) throws SQLException {
        return TheaterRowMapper.THEATER_ROW_MAPPER.map(rs);
        }

    @Override
    protected void mapEntityToTable(
            PreparedStatement ps,
            Theater theater
    ) throws SQLException {

        ps.setString(1, theater.getName());
        ps.setString(2, theater.getAddress());
        ps.setInt(3, theater.getFoundedYear());
        ps.setInt(4, theater.getAuditoriumCapacity());
        ps.setString(5, theater.getHistory());
        ps.setString(6, theater.getImagePath());
        ps.setLong(7, theater.getCountry().id());
        ps.setLong(8, theater.getCity().id());
    }



    }



