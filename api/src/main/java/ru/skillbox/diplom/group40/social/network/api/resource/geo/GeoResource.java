package ru.skillbox.diplom.group40.social.network.api.resource.geo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;


import java.util.List;
import java.util.UUID;



@RequestMapping("api/v1/geo")
public interface GeoResource {

    @PutMapping("/load")
    void load() throws Exception;

    @GetMapping("/country")
    ResponseEntity <List<CountryDto>> getCountries() throws Exception;



    @GetMapping("/country/{countryId}/city")
    ResponseEntity <List<CityDto>> getCitiesByCountryId(@PathVariable UUID countryId) throws Exception;


}
