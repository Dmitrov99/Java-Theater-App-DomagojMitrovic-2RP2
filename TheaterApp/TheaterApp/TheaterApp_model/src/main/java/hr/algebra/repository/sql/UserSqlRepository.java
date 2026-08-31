package hr.algebra.repository.sql;

import hr.algebra.model.Role;
import hr.algebra.model.User;
import hr.algebra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static hr.algebra.repository.sql.queries.UserQueries.*;

public class UserSqlRepository extends BaseSqlRepository<User> implements UserRepository {

    public UserSqlRepository(DataSource dataSource) {
        super(dataSource);
    }

    private static final Logger log = LoggerFactory.getLogger(UserSqlRepository.class);


    @Override
    public User create(User user) throws SQLException {
        long id = executeInsertReturningId(
                SQL_INSERT,
                ps -> mapEntityToTable(ps, user)
        );

        return new User(
                id,
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole()
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try {
            return findSingleResult(
                    SQL_FIND_BY_ID,
                    ps -> ps.setLong(1, id)
            );
        } catch (SQLException e) {
            log.error("Failed to find user with ID={}.", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }

        try {
            return findSingleResult(
                    SQL_FIND_BY_USERNAME,
                    ps -> ps.setString(1, username.trim())
            );
        } catch (SQLException e) {
            log.error("Failed to find user with username='{}'.", username, e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> retrieveAll() {
        try {
            return findMany(SQL_FIND_ALL, ps -> {});
        } catch (SQLException e) {
            log.error("Failed to retrieve users.", e);
            return List.of();
        }
    }

    @Override
    public int update(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }

        try {
            return executeUpdate(
                    SQL_UPDATE,
                    ps -> {
                        mapEntityToTable(ps, user);
                        ps.setLong(4, user.getId());
                    }
            );
        } catch (SQLException e) {
            log.error(
                    "Failed to update user with ID={}.",
                    user.getId(),
                    e
            );
            return 0;
        }
    }

    @Override
    public Optional<User> authenticate(String username, String password) throws SQLException {

        return findSingleResult(
                AUTHENTICATE,
                statement -> {
                    statement.setString(1, username);
                    statement.setString(2, password);
                }
        );
    }

    @Override
    public int delete(Long id) {
        if (id == null) {
            return 0;
        }

        try {
            return executeUpdate(
                    SQL_DELETE,
                    ps -> ps.setLong(1, id)
            );
        } catch (SQLException e) {
            log.error("Failed to delete user with ID={}.", id, e);
            return 0;
        }
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        String role = rs.getString("role");

        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                Role.valueOf(role)
        );
    }

    @Override
    protected void mapEntityToTable(PreparedStatement ps, User user) throws SQLException {

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPasswordHash());
        ps.setString(3, user.getRole().name());
    }
}
