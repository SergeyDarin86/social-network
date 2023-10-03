package ru.skillbox.diplom.group40.social.network.impl.resource.geo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.api.resource.geo.GeoResource;
import ru.skillbox.diplom.group40.social.network.impl.service.geo.GeoService;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class GeoResourceImpl implements GeoResource {
    private final GeoService geoServices;

    @Override
    public void load() {
        geoServices.load();
    }

    @Override
    public ResponseEntity<List<CountryDto>> getCountries() {
        return ResponseEntity.ok(geoServices.getCountries());
    }

    @Override
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(UUID countryId) {
        return ResponseEntity.ok(geoServices.getAllCitiesByCountryId(countryId));
    }

}
