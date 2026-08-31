package hr.algebra.repository.sql.mapper;

import hr.algebra.model.DirectionStyle;
import hr.algebra.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DirectorMapper implements RowMapper<Director> {

    public static final DirectorMapper DIRECTOR_MAPPER =
            new DirectorMapper();

    private DirectorMapper() {
    }

    @Override
    public Director map(ResultSet rs) throws SQLException {
        return new Director(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("oib"),
                DirectionStyle.valueOf(
                        rs.getString("direction_style")
                ),
                rs.getString("director_id")
        );
    }
}