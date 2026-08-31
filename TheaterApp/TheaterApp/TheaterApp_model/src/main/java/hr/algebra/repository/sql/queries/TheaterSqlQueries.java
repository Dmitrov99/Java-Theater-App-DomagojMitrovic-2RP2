package hr.algebra.repository.sql.queries;

public final class TheaterSqlQueries {
    private TheaterSqlQueries() {
    }

    public static final String ADD_ACTOR = """
        INSERT INTO theater_actor (theater_id, actor_id)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
        """;

    public static final String REMOVE_ACTOR = """
        DELETE FROM theater_actor
        WHERE theater_id = ?
          AND actor_id = ?
        """;

    public static final String FIND_ACTORS_BY_THEATER_ID = """
        SELECT
            a.id,
            a.first_name,
            a.last_name,
            a.oib,
            a.actor_id
        FROM actor a
        JOIN theater_actor ta ON ta.actor_id = a.id
        WHERE ta.theater_id = ?
        ORDER BY a.last_name, a.first_name
        """;

    public static final String ADD_DIRECTOR = """
        INSERT INTO theater_director (theater_id, director_id)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
        """;

    public static final String REMOVE_DIRECTOR = """
        DELETE FROM theater_director
        WHERE theater_id = ?
          AND director_id = ?
        """;

    public static final String FIND_DIRECTORS_BY_THEATER_ID = """
        SELECT
            d.id,
            d.first_name,
            d.last_name,
            d.oib,
            d.director_id,
            d.direction_style
        FROM director d
        JOIN theater_director td ON td.director_id = d.id
        WHERE td.theater_id = ?
        ORDER BY d.last_name, d.first_name
        """;
    public static final String SQL_SELECT_BASE = """
        SELECT
            t.id,
            t.name,
            t.address,
            t.founded_year,
            t.auditorium_capacity,
            t.history,
            t.image_path,
            c.id AS country_id,
            c.country_code,
            c.state_name,
            ci.id AS city_id,
            ci.name AS city_name,
            ci.postal_code
        FROM theater t
        JOIN country c ON c.id = t.country_id
        JOIN city ci ON ci.id = t.city_id
        """;
    public static final String SQL_FIND_BY_NAME = SQL_SELECT_BASE + """
        WHERE t.name ILIKE ?
        ORDER BY t.name, t.founded_year
        """;

    public static final String SQL_INSERT = """
        INSERT INTO theater (
            name,
            address,
            founded_year,
            auditorium_capacity,
            history,
            image_path,
            country_id,
            city_id
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING id
        """;

    public static final String SQL_UPDATE = """
        UPDATE theater
        SET name = ?,
            address = ?,
            founded_year = ?,
            auditorium_capacity = ?,
            history = ?,
            image_path = ?,
            country_id = ?,
            city_id = ?
        WHERE id = ?
        """;

    public static final String SQL_DELETE = """
        DELETE FROM theater
        WHERE id = ?
        """;



    public static final String SQL_FIND_ALL = SQL_SELECT_BASE + """
        ORDER BY t.name, t.founded_year
        """;

    public static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + """
        WHERE t.id = ?
        """;
}
