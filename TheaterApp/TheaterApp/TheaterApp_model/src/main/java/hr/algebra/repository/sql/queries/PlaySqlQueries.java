package hr.algebra.repository.sql.queries;

public final class PlaySqlQueries {

    private PlaySqlQueries() {
    }

    public static final String SELECT_BASE = """
            SELECT
                p.id,
                p.name,
                p.premier_date,
                p.performance_counter,
                p.play_type,

                d.id AS director_id,
                d.first_name AS director_first_name,
                d.last_name AS director_last_name,
                d.oib AS director_oib,
                d.director_id AS director_code,
                d.direction_style AS director_direction_style,

                t.id AS theater_id,
                t.name AS theater_name,
                t.address AS theater_address,
                t.founded_year AS theater_founded_year,
                t.auditorium_capacity AS theater_auditorium_capacity,
                t.history AS theater_history,
                t.image_path AS theater_image_path,

                c.id AS country_id,
                c.country_code,
                c.state_name,

                ci.id AS city_id,
                ci.name AS city_name,
                ci.postal_code
            FROM play p
            JOIN director d ON d.id = p.director_id
            JOIN theater t ON t.id = p.theater_id
            JOIN country c ON c.id = t.country_id
            JOIN city ci ON ci.id = t.city_id
            """;

    public static final String FIND_BY_ID = SELECT_BASE + """
            WHERE p.id = ?
            """;

    public static final String FIND_ALL = SELECT_BASE + """
            ORDER BY p.name, p.premier_date
            """;

    public static final String FIND_BY_NAME = SELECT_BASE + """
            WHERE p.name ILIKE ?
            ORDER BY p.name, p.premier_date
            """;

    public static final String INSERT = """
            INSERT INTO play (
                name,
                director_id,
                theater_id,
                premier_date,
                performance_counter,
                play_type
            )
            VALUES (?, ?, ?, ?, ?, ?::direction_style)
            RETURNING id
            """;

    public static final String UPDATE = """
            UPDATE play
            SET name = ?,
                director_id = ?,
                theater_id = ?,
                premier_date = ?,
                performance_counter = ?,
                play_type = ?::direction_style
            WHERE id = ?
            """;

    public static final String DELETE = """
            DELETE FROM play
            WHERE id = ?
            """;

    public static final String INCREMENT_PERFORMANCE_COUNTER = """
            UPDATE play
            SET performance_counter = performance_counter + 1
            WHERE id = ?
            """;

    public static final String ADD_ACTOR = """
            INSERT INTO play_actor (play_id, actor_id)
            VALUES (?, ?)
            ON CONFLICT DO NOTHING
            """;

    public static final String REMOVE_ACTOR = """
            DELETE FROM play_actor
            WHERE play_id = ?
              AND actor_id = ?
            """;

    public static final String FIND_ACTORS_BY_PLAY_ID = """
        SELECT
            a.id,
            a.first_name,
            a.last_name,
            a.oib,
            a.actor_id
        FROM actor a
        JOIN play_actor pa ON pa.actor_id = a.id
        WHERE pa.play_id = ?
        ORDER BY a.last_name, a.first_name
        """;
    public static final String FIND_BY_THEATER_ID = SELECT_BASE + """
        WHERE p.theater_id = ?
        ORDER BY p.premier_date, p.name
        """;


}