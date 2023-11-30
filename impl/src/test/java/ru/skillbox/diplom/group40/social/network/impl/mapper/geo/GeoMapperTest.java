package ru.skillbox.diplom.group40.social.network.impl.mapper.geo;

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
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoMapperTest {
    @Mock
    private CountryRepository countryRepository;

    private GeoMapper geoMapper;

    private FactoryData factoryData= new FactoryData();

    @BeforeEach
    void init(){
        geoMapper=new GeoMapperImpl();
    }



    @Test
    void cityToDto() {
        List<City> cityEntity = factoryData.createCityEntities();
        List<CityDto> expectedCityDtoList = factoryData.createCityDtos();
        List<CityDto> result = geoMapper.cityToDto(cityEntity);
        assertEquals(expectedCityDtoList.size(), result.size()); // проверяем совпадает ли количество
        assertNotNull(result);
        for (int i = 0; i < result.size(); i++) {  // в цикле сравниваем id  и названия стран
            assertEquals(expectedCityDtoList.get(i).getId(), result.get(i).getId());
            assertEquals(expectedCityDtoList.get(i).getTitle(), result.get(i).getTitle());
        }
    }


    @Test
    void countryToDto() {
        List<Country> countryList = factoryData.createCountryEntities(); // создаем лист сущностей, которые нужно преобразовать в дто
        List<CountryDto> expectedCountryDtoList = factoryData.createCountryDtos(); // создаем лист ожидаемых дто
        List<CountryDto> result= geoMapper.countryToDto(countryList); // вызываем метод countryToDto у geoMapper
        assertEquals(expectedCountryDtoList.size(), result.size()); // проверяем совпадает ли количество
        assertNotNull(result);
        for(int i=0; i< result.size(); i++){  // в цикле сравниваем id  и названия стран
            assertEquals(expectedCountryDtoList.get(i).getId(), result.get(i).getId());
            assertEquals(expectedCountryDtoList.get(i).getTitle(), result.get(i).getTitle());
        }
    }

    @Test
    void createCountry() {
        String title="Старна 1";
        Country expected=new Country();
        expected.setIsDeleted(false);
        expected.setTitle(title);
        when(countryRepository.save(any(Country.class))).thenReturn(expected);
        Country result = geoMapper.createCountry(null, title, countryRepository);
        assertEquals(expected, result);
        assertEquals(title, result.getTitle());
        assertFalse(result.getIsDeleted());
        verify(countryRepository).save(any(Country.class));
    }

    @Test
    void createCity() {
        List <City> cities=new ArrayList<>();
        String countryTitle= "Страна 1";
        Country country=new Country();
        country.setTitle(countryTitle);
        country.setIsDeleted(false);
        country.setId(factoryData.getCountryId1());
        String title="Город A";
        City expected=new City();
        expected.setIsDeleted(false);
        expected.setTitle(title);
        expected.setCountry(country);
        City result= geoMapper.createCity(null, country, title, cities);
        assertNotNull(result);
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getCountry(), result.getCountry());
        assertTrue(cities.contains(result));
    }

}