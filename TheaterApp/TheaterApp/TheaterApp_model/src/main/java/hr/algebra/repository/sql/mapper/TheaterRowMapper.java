package hr.algebra.repository.sql.mapper;

import hr.algebra.model.City;
import hr.algebra.model.Country;
import hr.algebra.model.Theater;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TheaterRowMapper implements RowMapper<Theater>{

    public static TheaterRowMapper THEATER_ROW_MAPPER=new TheaterRowMapper();
    private TheaterRowMapper() {
    }

    @Override
    public Theater map(ResultSet rs) throws SQLException {
        Country country = new Country(
                rs.getLong("country_id"),
                rs.getString("country_code"),
                rs.getString("state_name")
        );

        City city = new City(
                rs.getLong("city_id"),
                rs.getString("city_name"),
                rs.getString("postal_code")
        );

        return Theater.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .foundedYear(rs.getInt("founded_year"))
                .auditoriumCapacity(rs.getInt("auditorium_capacity"))
                .history(rs.getString("history"))
                .imagePath(rs.getString("image_path"))
                .country(country)
                .city(city)
                .build();
    }


}
