package hr.algebra.theaterapp_gui.util;

import hr.algebra.repository.sql.DataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


public final class DatabaseUtil {
    private static final DataSource DATA_SOURCE= DataSourceFactory.INSTANCE.getDataSource();

    private static final String INIT_SCHEMA="/sql/init-schema.sql";
    private DatabaseUtil() {
    }

    public static void initSchema() throws IOException, SQLException {
        try (InputStream inputStream =
                     DatabaseUtil.class.getResourceAsStream(INIT_SCHEMA);
             Connection connection = DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {

            if (inputStream == null) {
                throw new IOException(
                        INIT_SCHEMA + " couldn't be found"
                );
            }

            String ddl = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            for (String sql : ddl.split(";")) {
                String currentSql = sql.trim();

                if (!currentSql.isBlank()) {
                    statement.execute(currentSql);
                }
            }

        }
    }
}
