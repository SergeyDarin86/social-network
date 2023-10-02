package ru.skillbox.diplom.group40.social.network.impl.mapper.geo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface GeoMapper {

    List<CityDto> cityToDto(List<City> cityEntity);

    @Mapping(source = "country.id", target = "countryId")
    CityDto mapCityToDto(City city);

    List<CountryDto> countryToDto(List<Country> countryEntity);

    default Country createCountry(Country country, String countryTitle, CountryRepository countryRepository) {
        if (country == null) {
            Country newCountry = new Country();
            newCountry.setTitle(countryTitle);
            newCountry.setIsDeleted(false);
            return countryRepository.save(newCountry);
        }
        return country;
    }

    default City createCity(City city, Country country, String cityTitle, List<City> cities) {
        if (city == null) {
            city = new City();
            city.setTitle(cityTitle);
            city.setIsDeleted(false);
            city.setCountry(country);
            country.getCities().add(city);
            cities.add(city);
        }
        return city;
    }
}
