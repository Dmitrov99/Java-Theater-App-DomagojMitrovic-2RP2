package hr.algebra.repository;

import hr.algebra.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends EntityRepository<Country>{
    List<Country> findByName(String name);
    Optional<Country> findByCountryCode(String countryCode);
}
