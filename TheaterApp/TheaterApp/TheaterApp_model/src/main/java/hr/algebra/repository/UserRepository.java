package hr.algebra.repository;

import hr.algebra.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user) throws SQLException;

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> retrieveAll();

    Optional<User> authenticate(String username, String password) throws SQLException;
    int update(User user);

    int delete(Long id);
}