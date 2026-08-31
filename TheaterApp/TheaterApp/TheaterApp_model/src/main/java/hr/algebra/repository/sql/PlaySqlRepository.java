package hr.algebra.repository.sql;

import hr.algebra.model.*;
import hr.algebra.repository.PlayRepository;
import hr.algebra.repository.sql.mapper.PlayRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static hr.algebra.repository.sql.queries.PlaySqlQueries.*;
import static hr.algebra.repository.sql.mapper.ActorRowMapper.ACTOR_ROW_MAPPER;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlaySqlRepository extends BaseSqlRepository<Play> implements PlayRepository {
//todo napisi procedure za ove upite predugacko je za kod
    private static final Logger log = LoggerFactory.getLogger(PlaySqlRepository.class);


    public PlaySqlRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Play create(Play play) throws SQLException {
        long id = executeInsertReturningId(
                INSERT,
                ps -> mapEntityToTable(ps, play)
        );

        return play.toBuilder()
                .id(id)
                .build();
    }
    @Override
    public Optional<Play> findById(Long id) {
        try {
            return findSingleResult(
                    FIND_BY_ID,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to find play with ID={}.", id, e);
            return Optional.empty();
        }
    }
    @Override
    public List<Play> retrieveAll() {
        try {
            return findMany(FIND_ALL, ps -> {});

        } catch (SQLException e) {
            log.error("Failed to retrieve all plays.", e);
            return List.of();
        }
    }

    @Override
    public List<Play> findByName(String name) {
        try {
            return findMany(
                    FIND_BY_NAME,
                    ps -> ps.setString(1, "%" + name.trim() + "%")
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to find plays matching name '{}'.", name, e
            );
            return List.of();
        }
    }
    @Override
    public int update(Play play) {
        try {
            return executeUpdate(UPDATE,
                    ps -> {
                        mapEntityToTable(ps, play);
                        ps.setLong(7, play.getId());
                    }
            );

        } catch (SQLException e) {
            log.error("Failed to update play with ID={}.", play.getId(), e);
            return 0;
        }
    }
    @Override
    public int delete(Long id) {
        try {
            return executeUpdate(
                    DELETE,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to delete play with ID={}.", id, e);
            return 0;
        }
    }
    @Override
    public int performanceCounterIncrement(Long playId) {
        try {
            return executeUpdate(
                    INCREMENT_PERFORMANCE_COUNTER,
                    ps -> ps.setLong(1, playId)
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to increment performance counter for play ID={}.",
                    playId,
                    e
            );
            return 0;
        }
    }
    @Override
    public int addActorToPlay(Long playId, Long actorId) {
        try {
            return executeUpdate(
                    ADD_ACTOR,
                    ps -> {
                        ps.setLong(1, playId);
                        ps.setLong(2, actorId);
                    }
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to add actor ID={} to play ID={}.",
                    actorId,
                    playId,
                    e
            );
            return 0;
        }
    }
    @Override
    public int removeActorFromPlay(Long playId, Long actorId) {
        try {
            return executeUpdate(
                    REMOVE_ACTOR,
                    ps -> {
                        ps.setLong(1, playId);
                        ps.setLong(2, actorId);
                    }
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to remove actor ID={} from play ID={}.",
                    actorId,
                    playId,
                    e
            );
            return 0;
        }
    }
    @Override
    public List<Actor> findActorsByPlayId(Long playId) {
        try {
            return findMany(
                    FIND_ACTORS_BY_PLAY_ID,
                    ps -> ps.setLong(1, playId),
                    ACTOR_ROW_MAPPER
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to find actors for play ID={}.", playId, e);
            return List.of();
        }
    }


    @Override
    protected Play mapRow(ResultSet rs) throws SQLException {
    return PlayRowMapper.PLAY_ROW_MAPPER.map(rs);
    }
    @Override
    protected void mapEntityToTable(
            PreparedStatement ps,
            Play play
    ) throws SQLException {

        ps.setString(1, play.getName());
        ps.setLong(2, play.getDirector().getId());
        ps.setLong(3, play.getTheater().getId());
        ps.setObject(4, play.getPremierDate());
        ps.setInt(5, play.getPerformanceCounter());
        ps.setString(6, play.getPlayType().name());
    }
    @Override
    public List<Play> findPlaysByTheaterId(Long theaterId) {
        try {
            return findMany(
                    FIND_BY_THEATER_ID,
                    ps -> ps.setLong(1, theaterId)
            );

        } catch (SQLException e) {
            log.error("Failed to find plays for theater ID={}.", theaterId, e
            );
            return List.of();
        }
    }

}