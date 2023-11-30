package ru.skillbox.diplom.group40.social.network.impl.service.geo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.factoryData.FactoryData;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class GeoServiceTest {
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private GeoMapper geoMapper;

    private FactoryData factoryData= new FactoryData();
    private GeoService geoService;

    @BeforeEach
    void init(){
        geoService= new GeoService(cityRepository,countryRepository,geoMapper,null);
    }


    @Test
    void getCountries() {
        List<Country> countyEntityList = factoryData.createCountryEntities();
        List <CountryDto> countryDtoList = factoryData.createCountryDtos();
        when(countryRepository.findAllCountriesOrderByTitle()).thenReturn(countyEntityList);
        when(geoMapper.countryToDto(countyEntityList)).thenReturn(countryDtoList);
        List <CountryDto> result= geoService.getCountries();
        assertNotNull(result);
        assertEquals(countryDtoList, result);
    }

    @Test
    void getAllCitiesByCountryId() {
        List <City> cityEntityList = factoryData.createCityEntities();
        List <CityDto> cityDtoList = factoryData.createCityDtos();
        when(cityRepository.findByCountryIdOrderByTitle(factoryData.countryId1)).thenReturn(cityEntityList);
        when(geoMapper.cityToDto(cityEntityList)).thenReturn(cityDtoList);
        List <CityDto> result= geoService.getAllCitiesByCountryId(factoryData.countryId1);
        assertNotNull(result);
        assertEquals(cityDtoList, result);
    }

//    @Test
//    void isDataEmpty() {
//    }
//
//    @Test
//    void testLoadGeo() {
//    }
//
//    @Test
//    void loadGeo() {
//    }
}