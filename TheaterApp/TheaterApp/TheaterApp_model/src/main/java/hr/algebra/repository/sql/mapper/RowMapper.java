package hr.algebra.repository.sql.mapper;

import hr.algebra.model.Actor;

import java.sql.ResultSet;
import java.sql.SQLException;
//todo implementiraj mappere
@FunctionalInterface
public interface RowMapper <T>{
     T map(ResultSet rs) throws SQLException;

    //todo napravi ovo za sve umjesto map metode u svakoj klasi

}
