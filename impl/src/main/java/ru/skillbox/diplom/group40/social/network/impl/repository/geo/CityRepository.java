package ru.skillbox.diplom.group40.social.network.impl.repository.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityRepository extends BaseRepository<City> {
    List<City> findByCountryIdOrderByTitle(UUID country);
    City findByTitleAndCountry(String cityTitle, Country country);
}
