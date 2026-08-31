package hr.algebra.repository;

import hr.algebra.repository.sql.*;

import javax.sql.DataSource;

public final class RepositoryFactory {
    private static final DataSource DATA_SOURCE= DataSourceFactory.INSTANCE.getDataSource();
    private RepositoryFactory(){}

    public static CityRepository getCityRepository(){
        return new CitySqlRepository(DATA_SOURCE);
    }

    public static CountryRepository getCountryRepository() {
        return new CountrySqlRepository(DATA_SOURCE);
    }
    public static ActorRepository getActorRepository(){
        return new ActorSqlRepository(DATA_SOURCE);
    }

    public static DirectorRepository getDirectorRepository(){
        return new DirectorSqlRepository(DATA_SOURCE);
    }
    public static PlayRepository getPlayRepository(){
        return new PlaySqlRepository(DATA_SOURCE);
    }
    public static TheaterRepository getTheaterRepository(){
        return new TheaterSqlRepository(DATA_SOURCE);
    }

    public static UserRepository getUserRepository() { return new UserSqlRepository(DATA_SOURCE);
    }
}
