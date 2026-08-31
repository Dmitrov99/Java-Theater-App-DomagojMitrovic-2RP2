package hr.algebra.repository.sql;

import hr.algebra.model.Country;
import hr.algebra.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CountrySqlRepository extends BaseSqlRepository<Country> implements CountryRepository {

    private static final Logger log = LoggerFactory.getLogger(CountrySqlRepository.class);



    public CountrySqlRepository(DataSource dataSource) {
        super(dataSource);
    }

    private static final String SQL_FIND_BY_NAME = """
        SELECT id, country_code, state_name
        FROM country
        WHERE state_name ILIKE ?
        ORDER BY state_name
        """;
    private static final String SQL_FIND_BY_COUNTRY_CODE = """
        SELECT id, country_code, state_name
        FROM country
        WHERE country_code = ?
        """;

    private static final String SQL_INSERT = """
        INSERT INTO country (country_code, state_name)
        VALUES (?, ?)
        RETURNING id
        """;

    private static final String SQL_FIND_BY_ID = """
        SELECT id, country_code, state_name
        FROM country
        WHERE id = ?
        """;

    private static final String SQL_FIND_ALL = """
        SELECT id, country_code, state_name
        FROM country
        ORDER BY state_name, country_code
        """;

    private static final String SQL_UPDATE = """
        UPDATE country
        SET country_code = ?,
            state_name = ?
        WHERE id = ?
        """;

    private static final String SQL_DELETE = """
        DELETE FROM country
        WHERE id = ?
        """;

    private static final String SQL_COUNT = """
        SELECT COUNT(*)
        FROM country
        """;


    @Override
    public List<Country> findByName(String name) {
        try {
            return findMany(
                    SQL_FIND_BY_NAME,
                    ps -> ps.setString(1, "%" + name + "%")
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to find countries matching name '{}'.",
                    name,
                    e
            );

            return List.of();
        }
    }
    @Override
    public Optional<Country> findByCountryCode(String countryCode) {
        try {
            return findSingleResult(
                    SQL_FIND_BY_COUNTRY_CODE,
                    ps -> ps.setString(1, countryCode)
            );

        } catch (SQLException e) {
            log.error(
                    "Failed to find country with code '{}'.",
                    countryCode,
                    e
            );

            return Optional.empty();
        }
    }

    @Override
    public Country create(Country country) throws SQLException {

        if(country==null||country.stateName().isBlank()||country.stateName().trim().isEmpty())throw new IllegalArgumentException("Country can't be null");

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            mapEntityToTable(ps,country);

            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    long generatedId = rs.getLong("id");

                    return new Country(
                            generatedId,
                            country.countryCode(),
                            country.stateName()
                    );
                }
            }

            throw new SQLException(
                    "Creating country failed; generated ID was not returned."
            );

        }
        catch (SQLException e)
        {
            log.error(
                    "Failed to create country with code '{}' and name '{}'.",
                    country.countryCode(),
                    country.stateName(),
                    e
            );
            throw e;
        }
    }

    @Override
    public Optional<Country> findById(Long id) {
        if (id == null) {
            log.warn("Cannot find country: ID is null.");
            return Optional.empty();
        }

        try {
            return findSingleResult(
                    SQL_FIND_BY_ID,
                    ps -> ps.setLong(1, id)
            );

        } catch (SQLException e) {
            log.error("Failed to find country with ID={}.", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Country> retrieveAll() {
        try {
            return findMany(
                    SQL_FIND_ALL,
                    ps -> {
                        // No SQL parameters.
                    }
            );

        } catch (SQLException e) {
            log.error("Failed to retrieve all countries.", e);
            return List.of();
        }
    }

    @Override
    public int update(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("Country must not be null.");
        }

        if (country.id() == null) {
            throw new IllegalArgumentException(
                    "Cannot update country without an ID."
            );
        }

        try {


            return executeUpdate(SQL_UPDATE, ps -> {
                ps.setString(1, country.countryCode());
                ps.setString(2, country.stateName());
                ps.setLong(3, country.id());
            });

        } catch (SQLException e) {
            log.error(
                    "Failed to update country with ID={}.",
                    country.id(),
                    e
            );
            return 0;
        }
    }

    @Override
    public int delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Country ID must not be null.");
        }

        try {
            int deletedRows = executeUpdate(
                    SQL_DELETE,
                    ps -> ps.setLong(1, id)
            );

            if (deletedRows == 1) {
                log.info("Country deleted successfully: ID={}.", id);
            } else {
                log.warn(
                        "No country was deleted because ID={} was not found.",
                        id
                );
            }

            return deletedRows;

        } catch (SQLException e) {
            log.error("Failed to delete country with ID={}.", id, e);
            return 0;
        }
    }
//
//    @Override
//    public long count() {
//        try (Connection con = dataSource.getConnection();
//             PreparedStatement ps = con.prepareStatement(SQL_COUNT);
//             ResultSet rs = ps.executeQuery()) {
//
//            if (rs.next()) {
//                return rs.getLong(1);
//            }
//
//        } catch (SQLException e) {
//            log.error("Failed to count countries.", e);
//        }
//
//        return 0;
//    }

    @Override
    protected Country mapRow(ResultSet rs) throws SQLException {
        return new Country(
                rs.getLong("id"),
                rs.getString("country_code"),
                rs.getString("state_name")
        );
    }

    @Override
    protected void mapEntityToTable(PreparedStatement ps, Country country) throws SQLException {
        ps.setString(1, country.countryCode());
        ps.setString(2, country.stateName());
    }
}
