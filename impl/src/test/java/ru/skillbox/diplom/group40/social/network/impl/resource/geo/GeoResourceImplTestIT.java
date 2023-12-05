package ru.skillbox.diplom.group40.social.network.impl.resource.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.geo.GeoService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("integrationtest")
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = true, addFilters = false)
@Testcontainers
//@ContextConfiguration(initializers = {GeoResourceImplTestIT.class})
public class GeoResourceImplTestIT {

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

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.4")
            .withDatabaseName("skillbox")
            .withUsername("skillbox")
            .withPassword("skillbox")
            .withInitScript("db.sql");

    @DynamicPropertySource
    static void registerPostgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void getCountry() throws Exception{
        var requestBuilder= get("/api/v1/geo/country");
        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(containsString("7c124018-a4e0-49e1-b2ea-6479a5fad511")),
                        content().string(containsString("dd515e3d-ccdf-4941-bef7-0b55bb5c2fcd")),
                        content().string(containsString("bf3c2cea-6816-43ac-8b08-f0b2669c735b"))
                );
    }
    @Test
    void getAllCitiesByCountryId()throws Exception{
        mockMvc.perform(get("/api/v1/geo/country/{countryId}/city", "7c124018-a4e0-49e1-b2ea-6479a5fad511")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("7c124018-a4e0-49e1-b2ea-6479a5fad511")));

    }


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

}