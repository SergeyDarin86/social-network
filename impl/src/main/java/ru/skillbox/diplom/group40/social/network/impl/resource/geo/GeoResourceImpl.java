package ru.skillbox.diplom.group40.social.network.impl.resource.geo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.api.resource.geo.GeoResource;
import ru.skillbox.diplom.group40.social.network.impl.service.geo.GeoService;
import ru.skillbox.diplom.group40.social.network.impl.utils.feignClient.GeoAdapterClient;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class GeoResourceImpl implements GeoResource {
    private final GeoService geoServices;
    private final GeoAdapterClient geoAdapterClient;

    @Override
    public void load() {
    geoAdapterClient.load();
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
