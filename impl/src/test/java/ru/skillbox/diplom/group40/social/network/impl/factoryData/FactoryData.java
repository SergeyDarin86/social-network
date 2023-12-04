package ru.skillbox.diplom.group40.social.network.impl.factoryData;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.KafkaListeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
public class FactoryData {
    private UUID countryId1 =UUID.randomUUID();
    private UUID countryId2 =UUID.randomUUID();
    private UUID cityId1 =UUID.randomUUID();
    private UUID cityId2 =UUID.randomUUID();


    public List<Country> createCountryEntities(){
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
    public List <CountryDto> createCountryDtos(){
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
     public List <City> createCityEntities(){
        Country country1 = new Country();
        country1.setId(countryId1);
        country1.setTitle("Country 1");
        country1.setIsDeleted(false);
        List<City> cityEntityList=new ArrayList<>();
        City city1=new City();
        city1.setId(cityId1);
        city1.setTitle("Город А");
        city1.setCountry(country1);
        city1.setIsDeleted(false);
        cityEntityList.add(city1);
        City city2=new City();
        city2.setId(cityId2);
        city2.setTitle("Город Б");
        city2.setCountry(country1);
        city2.setIsDeleted(false);
        cityEntityList.add(city2);
        country1.setCities(cityEntityList);
        return cityEntityList;
    }
     public List <CityDto> createCityDtos(){
        List<CityDto> cityDtoList=new ArrayList<>();
        CityDto city1=new CityDto();
        city1.setId(cityId1);
        city1.setTitle("Город А");
        city1.setCountryId(countryId1);
        city1.setIsDeleted(false);
        cityDtoList.add(city1);
        CityDto city2=new CityDto();
        city2.setId(cityId2);
        city2.setTitle("Город Б");
        city2.setCountryId(countryId1);
        city2.setIsDeleted(false);
        cityDtoList.add(city2);
        return cityDtoList;
    }


}
