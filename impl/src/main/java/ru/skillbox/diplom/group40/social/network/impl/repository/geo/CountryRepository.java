package ru.skillbox.diplom.group40.social.network.impl.repository.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CountryRepository extends BaseRepository<Country> {
    @Query("SELECT c FROM Country c ORDER BY c.title")
    List<Country> findAllCountriesOrderByTitle();
    Country findByTitle(String countryTitle);
}
