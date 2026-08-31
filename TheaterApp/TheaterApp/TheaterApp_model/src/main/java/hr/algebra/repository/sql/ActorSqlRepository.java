package hr.algebra.repository.sql;

import hr.algebra.model.Actor;
import hr.algebra.repository.ActorRepository;
import hr.algebra.repository.sql.mapper.ActorRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ActorSqlRepository extends BaseSqlRepository<Actor> implements ActorRepository {

    Logger log= LoggerFactory.getLogger(ActorSqlRepository.class);
    public ActorSqlRepository(DataSource dataSource) {
        super(dataSource);
    }
    private static final String SQL_SELECT_BASE = """
        SELECT id, first_name, last_name, oib, actor_id
        FROM actor
        """;
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + """
        ORDER BY last_name, first_name, actor_id
        """;

    private static final String SQL_FIND_BY_NAME = SQL_SELECT_BASE + """
        WHERE first_name ILIKE ?
           OR last_name ILIKE ?
           OR first_name || ' ' || last_name ILIKE ?
        ORDER BY last_name, first_name, actor_id
        """;

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + """
        WHERE id = ?
        """;

    private static final String SQL_INSERT = """
        INSERT INTO actor (first_name, last_name, oib, actor_id)
        VALUES (?, ?, ?, ?)
        RETURNING id
        """;
    private static final String SQL_UPDATE = """
        UPDATE actor
        SET first_name = ?,
            last_name = ?,
            oib = ?,
            actor_id = ?
        WHERE id = ?
        """;



    private static final String SQL_DELETE = """
        DELETE FROM actor
        WHERE id = ?
        """;
//    @Override
//    public Optional<Actor> findByActorId(String actorId) {
//        try {
//            return findSingleResult(SQL_FIND_BY_ID,ps->ps.setString(1,actorId));
//        } catch (SQLException e) {
//            log.error("Error retriving actor ActorID {}.", actorId, e);
//            return Optional.empty();
//        }
//    }

    @Override
    public List<Actor> findByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }

        try {
            String searchTerm = "%" + name.trim() + "%";

            return findMany(
                    SQL_FIND_BY_NAME,
                    ps -> {
                        ps.setString(1, searchTerm);
                        ps.setString(2, searchTerm);
                        ps.setString(3, searchTerm);
                    }
            );

        } catch (SQLException e) {
            log.error("Failed to find actors matching name '{}'.", name, e);
            return List.of();
        }
    }

    @Override
    public Actor create(Actor actor) throws SQLException {
                    long generatedId = executeInsertReturningId(SQL_INSERT,ps->mapEntityToTable(ps,actor));

                    return new Actor(
                            generatedId,
                            actor.getFirstName(),
                            actor.getLastName(),
                            actor.getOib(),
                            actor.getActorId()
                    );
                }


    @Override
    public Optional<Actor> findById(Long id) {
        try {
            return findSingleResult(SQL_FIND_BY_ID,ps->ps.setLong(1,id));
        } catch (SQLException e) {
            log.error("Error retriving actor ActorID {}.", id, e);
            return Optional.empty();
        }
    }
    @Override
    public List<Actor> retrieveAll() {
        try {
            return findMany(
                    SQL_FIND_ALL,
                    ps -> {}
            );

        } catch (SQLException e) {
            log.error("Failed to retrieve all actors.", e);
            return List.of();
        }
    }

    @Override
    public int update(Actor actor) {
        if (actor == null) {
            throw new IllegalArgumentException("Actor must not be null.");
        }

        if (actor.getId() == null) {
            throw new IllegalArgumentException(
                    "Cannot update actor without an ID."
            );
        }

        try {
            return executeUpdate(
                    SQL_UPDATE,
                    ps -> {
                        ps.setString(1, actor.getFirstName());
                        ps.setString(2, actor.getLastName());
                        ps.setString(3, actor.getOib());
                        ps.setString(4, actor.getActorId());
                        ps.setLong(5, actor.getId());
                    }
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to update actor with ID={}.",
                    actor.getId(),
                    e
            );
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Actor ID must not be null.");
        }

        try {
            return executeUpdate(
                    SQL_DELETE,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to delete actor with ID={}.", id, e);
            return 0;
        }
    }


    @Override
    protected Actor mapRow(ResultSet rs) throws SQLException {
   return ActorRowMapper.ACTOR_ROW_MAPPER.map(rs);
    }

    @Override
    protected void mapEntityToTable(PreparedStatement ps, Actor actor) throws SQLException {

        ps.setString(1, actor.getFirstName());
        ps.setString(2, actor.getLastName());
        ps.setString(3, actor.getOib());
        ps.setString(4, actor.getActorId());
    }
    //todo greska je u constraintu baze. Prepravi i onda koristi ove metode za automatsko generiranje act
    private String generateNextActorId() {
        int highestNumber = retrieveAll()
                .stream()
                .map(Actor::getActorId)
                .mapToInt(actorId ->
                        Integer.parseInt(actorId.substring(4))
                )
                .max()
                .orElse(0);

        return String.format("ACT-%03d", highestNumber + 1);
    }
}
