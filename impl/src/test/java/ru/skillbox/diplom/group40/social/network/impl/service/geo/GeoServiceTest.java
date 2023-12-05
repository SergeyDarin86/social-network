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
import ru.skillbox.diplom.group40.social.network.impl.factoryData.GeoFactoryData;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoServiceTest {
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private GeoMapper geoMapper;

    private GeoFactoryData geoFactoryData = new GeoFactoryData();
    private GeoService geoService;


    @BeforeEach
    void init(){
        geoService= new GeoService(cityRepository,countryRepository,geoMapper, new ObjectMapper());
    }

    @Test
    void getCountries() {
        List<Country> countyEntityList = geoFactoryData.createCountryEntities();
        List <CountryDto> countryDtoList = geoFactoryData.createCountryDtos();
        when(countryRepository.findAllCountriesOrderByTitle()).thenReturn(countyEntityList);
        when(geoMapper.countryToDto(countyEntityList)).thenReturn(countryDtoList);
        List <CountryDto> result= geoService.getCountries();
        assertNotNull(result);
        assertEquals(countryDtoList, result);
    }

    @Test
    void getAllCitiesByCountryId() {
        List <City> cityEntityList = geoFactoryData.createCityEntities();
        List <CityDto> cityDtoList = geoFactoryData.createCityDtos();
        when(cityRepository.findByCountryIdOrderByTitle(geoFactoryData.getCountryId1())).thenReturn(cityEntityList);
        when(geoMapper.cityToDto(cityEntityList)).thenReturn(cityDtoList);
        List <CityDto> result= geoService.getAllCitiesByCountryId(geoFactoryData.getCountryId1());
        assertNotNull(result);
        assertEquals(cityDtoList, result);
    }
}