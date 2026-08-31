package hr.algebra.repository.sql.mapper;

import hr.algebra.model.Actor;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ActorRowMapper implements RowMapper<Actor>  {

    public static final ActorRowMapper ACTOR_ROW_MAPPER=new ActorRowMapper();//todo zasto public static final?

    private ActorRowMapper() {
    }

    //todo napravi ovo za sve umjesto map metode u svakoj klasi
    @Override
    public Actor map(ResultSet rs) throws SQLException {
        return new Actor(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("oib"),
                rs.getString("actor_id")
        );
    }
}