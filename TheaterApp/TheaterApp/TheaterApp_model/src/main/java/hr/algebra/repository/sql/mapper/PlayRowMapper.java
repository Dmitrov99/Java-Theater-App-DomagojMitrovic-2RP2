package hr.algebra.repository.sql.mapper;

import hr.algebra.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class PlayRowMapper implements RowMapper<Play>{

    public static final PlayRowMapper PLAY_ROW_MAPPER=new PlayRowMapper();

    private PlayRowMapper() {
    }

    public Play map(ResultSet rs) throws SQLException {
        Director director = new Director(
                rs.getLong("director_id"),
                rs.getString("director_first_name"),
                rs.getString("director_last_name"),
                rs.getString("director_oib"),
                DirectionStyle.valueOf(
                        rs.getString("director_direction_style")
                ),
                rs.getString("director_code")
        );

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

        Theater theater = Theater.builder()
                .id(rs.getLong("theater_id"))
                .name(rs.getString("theater_name"))
                .address(rs.getString("theater_address"))
                .foundedYear(rs.getInt("theater_founded_year"))
                .auditoriumCapacity(
                        rs.getInt("theater_auditorium_capacity")
                )
                .history(rs.getString("theater_history"))
                .imagePath(rs.getString("theater_image_path"))
                .country(country)
                .city(city)
                .build();

        return Play.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .director(director)
                .theater(theater)
                .premierDate(
                        rs.getObject("premier_date", LocalDate.class)
                )
                .performanceCounter(
                        rs.getInt("performance_counter")
                )
                .playType(
                        DirectionStyle.valueOf(rs.getString("play_type"))
                )
                .build();
    }
}
