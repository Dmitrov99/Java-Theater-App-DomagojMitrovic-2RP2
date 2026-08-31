package hr.algebra.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BaseRepository<T> {
    T create(T entity) throws SQLException;
    Optional<T> findById(Long id);
    List<T> retrieveAll();
    int update(T entity);
    int delete(Long id);

}
