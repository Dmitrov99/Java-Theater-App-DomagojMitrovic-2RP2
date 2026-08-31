package hr.algebra.repository.sql.mapper;

import hr.algebra.model.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityMapper implements RowMapper<City> {
    @Override
    public City map(ResultSet rs) throws SQLException {
        return new City(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("postal_code")
        );
    }
}
