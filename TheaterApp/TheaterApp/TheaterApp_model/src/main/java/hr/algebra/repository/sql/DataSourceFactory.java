package hr.algebra.repository.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum DataSourceFactory {
    INSTANCE;
    private static final String PROPERTIES="/db.properties";
    private static final Logger log= LoggerFactory.getLogger(DataSourceFactory.class);

    private final DataSource dataSource;
    DataSourceFactory(){
        this.dataSource=createDataSource();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private DataSource createDataSource(){
        Properties props=new Properties();
        try (InputStream is=DataSourceFactory.class.getResourceAsStream(PROPERTIES)) {
            if(is==null){
                throw new IllegalStateException("db.prop nije pronaden");
            }
            props.load(is);

            HikariConfig config=new HikariConfig();
            String host=props.getProperty("PG_HOST");
            String port=props.getProperty("PG_PORT");
            String database=props.getProperty("PG_DATABASE");
            String jdbcUrl=String.format("jdbc:postgresql://%s:%s/%s",host,port,database);
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(props.getProperty("PG_USER"));
            config.setPassword(props.getProperty("PG_PASSWORD"));
            //hikari

            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("POOL_MAXIMUM_POOL_SIZE")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("POOL_MINIMUM_IDLE")));
            config.setConnectionTimeout(Integer.parseInt(props.getProperty("POOL_CONNECTION_TIMEOUT")));
            config.setIdleTimeout(Integer.parseInt(props.getProperty("POOL_IDLE_TIMEOUT")));
            config.setMaxLifetime(Integer.parseInt(props.getProperty("POOL_MAX_LIFETIME")));
            return new HikariDataSource(config);
        } catch (IOException e) { //TODO dodaj logging i excpetion custom
            throw new RuntimeException(e);
        }


    }
}

