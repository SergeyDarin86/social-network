package ru.skillbox.diplom.group40.social.network.impl.service.geo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class GeoServiceTestIntegration {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    private GeoMapper geoMapper = new GeoMapperImpl();
    //    @InjectMocks
    @InjectMocks
    @Autowired
    private GeoService geoService;

    private final UUID countryId1 = UUID.randomUUID();
    private final UUID countryId2 = UUID.randomUUID();
    private final UUID cityId1 = UUID.randomUUID();
    private final UUID cityId2 = UUID.randomUUID();


    @Test
    void testGetCountries() {
        List<Country> countyEntityList = countryRepository.findAllCountriesOrderByTitle();
        List<CountryDto> countryDtoList = geoMapper.countryToDto(countyEntityList);
//        when(countryRepository.findAllCountriesOrderByTitle()).thenReturn(countyEntityList);
//        when(geoMapper.countryToDto(countyEntityList)).thenReturn(countryDtoList);
        List <CountryDto> result= geoService.getCountries();
        //       List <Country> fjdfjdfgjfndjfnd=countryRepository.findAllCountriesOrderByTitle();
        assertEquals(countryDtoList, result);
    }

    @Test
    void testGetAllCitiesByCountryId() {
        List<City> cityEntityList = cityRepository.findByCountryIdOrderByTitle(countryId1);
        List<CityDto> cityDtoList = geoMapper.cityToDto(cityEntityList);
//        when(cityRepository.findByCountryIdOrderByTitle(countryId1)).thenReturn(cityEntityList);
//        when(geoMapper.cityToDto(cityEntityList)).thenReturn(cityDtoList);
        List<CityDto> result = geoService.getAllCitiesByCountryId(countryId1);
        assertEquals(cityDtoList, result);
    }

    @Test
    void testCityToCityDto() {
        List<City> cityEntity = createCityEntities();
        List<CityDto> expectedCityDtoList = createCityDtos();
        List<CityDto> result = geoMapper.cityToDto(cityEntity);
        assertEquals(2, result.size()); // проверяем совпадает ли количество
        for (int i = 0; i < result.size(); i++) {  // в цикле сравниваем id  и названия стран
            assertEquals(expectedCityDtoList.get(i).getId(), result.get(i).getId());
            assertEquals(expectedCityDtoList.get(i).getTitle(), result.get(i).getTitle());
        }
    }


    @Test
    void testCountryToCountryDto() {
        List<Country> countryList = createCountryEntities(); // создаем лист сущностей, которые нужно преобразовать в дто
        List<CountryDto> expectedCountryDtoList = createCountryDtos(); // создаем лист ожидаемых дто
        // чтоб при вызове метода, возвращался expectedCountryDtoList
        List<CountryDto> result = geoMapper.countryToDto(countryList); // вызываем метод countryToDto у geoMapper
        assertEquals(2, result.size()); // проверяем совпадает ли количество
        for (int i = 0; i < result.size(); i++) {  // в цикле сравниваем id  и названия стран
            assertEquals(expectedCountryDtoList.get(i).getId(), result.get(i).getId());
            assertEquals(expectedCountryDtoList.get(i).getTitle(), result.get(i).getTitle());
        }

    }

    private List<Country> createCountryEntities() {
        List<Country> countryEntitiesList = new ArrayList<>();
        Country country1 = new Country();
        country1.setId(countryId1);
        country1.setTitle("Country 1");
        Country country2 = new Country();
        country2.setId(countryId2);
        country2.setTitle("Country 2");
        countryEntitiesList.add(country1);
        countryEntitiesList.add(country2);
        return countryEntitiesList;
    }

    private List<CountryDto> createCountryDtos() {
        List<CountryDto> countryDtoList = new ArrayList<>();
        CountryDto countryDto1 = new CountryDto();
        countryDto1.setId(countryId1);
        countryDto1.setTitle("Country 1");
//        countryDto1.setCities(List.of());

        CountryDto countryDto2 = new CountryDto();
        countryDto2.setId(countryId2);
        countryDto2.setTitle("Country 2");
//        countryDto2.setCities(List.of());

        countryDtoList.add(countryDto1);
        countryDtoList.add(countryDto2);
        return countryDtoList;
    }

    private List<City> createCityEntities() {
        Country country1 = new Country();
        country1.setId(countryId1);
        country1.setTitle("Country 1");
        country1.setIsDeleted(false);
        List<City> cityEntityList = new ArrayList<>();
        City city1 = new City();
        city1.setId(cityId1);
        city1.setTitle("Город А");
        city1.setCountry(country1);
        city1.setIsDeleted(false);
        cityEntityList.add(city1);
        City city2 = new City();
        city2.setId(cityId2);
        city2.setTitle("Город Б");
        city2.setCountry(country1);
        city2.setIsDeleted(false);
        cityEntityList.add(city2);
        country1.setCities(cityEntityList);
        return cityEntityList;
    }

    private List<CityDto> createCityDtos() {
        List<CityDto> cityDtoList = new ArrayList<>();
        CityDto city1 = new CityDto();
        city1.setId(cityId1);
        city1.setTitle("Город А");
        city1.setCountryId(countryId1);
        city1.setIsDeleted(false);
        cityDtoList.add(city1);
        CityDto city2 = new CityDto();
        city2.setId(cityId2);
        city2.setTitle("Город Б");
        city2.setCountryId(countryId1);
        city2.setIsDeleted(false);
        cityDtoList.add(city2);
        return cityDtoList;
    }

}