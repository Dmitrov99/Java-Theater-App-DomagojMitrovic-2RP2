package hr.algebra.repository;


import java.util.List;


public interface EntityRepository<T> extends BaseRepository<T>{
    List<T> findByName(String name);
}
