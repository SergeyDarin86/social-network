package ru.skillbox.diplom.group40.social.network.impl.repository.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.UUID;

@Repository
public interface CountryRepository extends BaseRepository<Country> {
    Country findAllById(UUID id);
    Country findByTitle(String countryTitle);
}
