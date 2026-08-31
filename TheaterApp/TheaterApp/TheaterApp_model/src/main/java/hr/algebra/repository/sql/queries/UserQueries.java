package hr.algebra.repository.sql.queries;

public final class UserQueries {
    private UserQueries(){};

    public static final String SQL_SELECT_BASE = """
            SELECT id, username, password, role
            FROM app_user
            """;

    public static final String SQL_FIND_ALL = SQL_SELECT_BASE + """
            ORDER BY username
            """;

    public static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + """
            WHERE id = ?
            """;

    public static final String SQL_FIND_BY_USERNAME = SQL_SELECT_BASE + """
            WHERE username = ?
            """;

    public static final String SQL_INSERT = """
            INSERT INTO app_user (username, password, role)
            VALUES (?, ?, ?::user_role)
            RETURNING id
            """;

    public static final String SQL_UPDATE = """
            UPDATE app_user
            SET username = ?,
                password = ?,
                role = ?::user_role
            WHERE id = ?
            """;

    public static final String SQL_DELETE = """
            DELETE FROM app_user
            WHERE id = ?
            """;
    public static final String AUTHENTICATE = """
        SELECT id, username, password_hash, role
        FROM app_user
        WHERE username = ?
          AND password_hash = ?
        """;
}
