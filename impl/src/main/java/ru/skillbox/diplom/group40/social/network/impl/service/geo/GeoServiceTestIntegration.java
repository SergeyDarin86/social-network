package ru.skillbox.diplom.group40.social.network.impl.service.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = true, addFilters = false)
class GeoServiceTestIntegration {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    private GeoMapper geoMapper = new GeoMapperImpl();
    @InjectMocks
    @Autowired
    private GeoService geoService;

    private final UUID countryId1 = UUID.randomUUID();
    private final UUID countryId2 = UUID.randomUUID();
    private final UUID cityId1 = UUID.randomUUID();
    private final UUID cityId2 = UUID.randomUUID();

    @Test
    void getCountry() throws Exception{
    var requestBuilder= get("/api/v1/geo/country");
    this.mockMvc.perform(requestBuilder)
            .andDo(print())
        .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON)
//                jsonPath("title").value("Россия")
//                content().string(contains("Россия")),
//                content().string(contains("Беларусь"))
                , content().string(containsString("f3218785-a64a-4c4f-8ba9-e997bf178d1c"))
                , content().string(containsString("017bd875-76d9-4aef-9673-c352e46934de"))
                , content().string(containsString("3219f340-c0a4-483a-a9f8-85594f02e2af"))
        );
}
    @Test
    void getAllCitiesByCountryId()throws Exception{
    mockMvc.perform(get("/api/v1/geo/country/{countryId}/city", "f3218785-a64a-4c4f-8ba9-e997bf178d1c")
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("f3218785-a64a-4c4f-8ba9-e997bf178d1c")));

}

//    @Test
//    void loadGeo(){
//        mockMvc.perform(put("/api/v1/geo//load"))
//    }


    @Test
    void testGetCountries() {
        List<Country> countyEntityList = countryRepository.findAllCountriesOrderByTitle();
        List<CountryDto> countryDtoList = geoMapper.countryToDto(countyEntityList);
        List <CountryDto> result= geoService.getCountries();
        assertEquals(countryDtoList, result);
    }

    @Test
    void testGetAllCitiesByCountryId() {
        List<City> cityEntityList = cityRepository.findByCountryIdOrderByTitle(countryId1);
        List<CityDto> cityDtoList = geoMapper.cityToDto(cityEntityList);
        List<CityDto> result = geoService.getAllCitiesByCountryId(countryId1);
        assertEquals(cityDtoList, result);
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

        CountryDto countryDto2 = new CountryDto();
        countryDto2.setId(countryId2);
        countryDto2.setTitle("Country 2");

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