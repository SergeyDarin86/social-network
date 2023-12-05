package ru.skillbox.diplom.group40.social.network.impl.service.geo;


import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GeoMapper geoMapper;
    private final ObjectMapper objectMapper;

    @Cacheable(cacheNames = "countriesCache", key = "'allCountries'")
    public List<CountryDto> getCountries() {
        log.info("Start method getCountries: ");
        List<Country> countryList = countryRepository.findAllCountriesOrderByTitle();
        List<CountryDto> countryDtoList = geoMapper.countryToDto(countryList);
        log.info("Количество стран: {}", countryDtoList.size());
        return countryDtoList;
    }


    @Cacheable(cacheNames = "citiesCache", key = "#id")
    public List<CityDto> getAllCitiesByCountryId(UUID id) {
        log.info("Страна с id= {}", id);
        List<City> cityList = cityRepository.findByCountryIdOrderByTitle(id);
        List<CityDto> cityDtoList = geoMapper.cityToDto(cityList);
        log.info("Найдено городов в стране с id= {}: {} ", id, cityDtoList.size());
        return cityDtoList;
    }

    public boolean isDataEmpty() {
        return countryRepository.count() == 0 || cityRepository.count() == 0;
    }

    @CacheEvict(cacheNames = {"countriesCache", "citiesCache"}, allEntries = true)
    public void testLoadGeo(){
        Map<String, Country> countryMap = new HashMap<>();
        List<City> citiesToSave=new ArrayList<>();
        String countryTitle= "Россия";
        String cityTitle="Москва";
        Country country = countryMap.computeIfAbsent(countryTitle, this::loadCountry);
        City city = loadCity(cityTitle, country, citiesToSave);
        if (!citiesToSave.isEmpty()) {
            cityRepository.saveAll(citiesToSave);
        }
        log.info("Количество стран: {} , Количество городов: {}", countryRepository.count(), cityRepository.count());
    }
    @CacheEvict(cacheNames = {"countriesCache", "citiesCache"}, allEntries = true)
    public void loadGeo(String message) {
        Map<String, Country> countryMap = new ConcurrentHashMap<>();
        List<City> citiesToSave = Collections.synchronizedList(new ArrayList<>());

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(8);

            CountryDto countryDto = objectMapper.readValue(message, CountryDto.class);


            String countryTitle = countryDto.getTitle();
            Country country = countryMap.computeIfAbsent(countryTitle, this::loadCountry);
            List<CityDto> cities = countryDto.getCities();

            for (CityDto cityDto : cities) {
                executorService.submit(() -> {
                    String cityTitle = cityDto.getTitle();
                    City city = loadCity(cityTitle, country, citiesToSave);
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            if (!citiesToSave.isEmpty()) {
                cityRepository.saveAll(citiesToSave);
            }
            log.info("Количество стран: {} , Количество городов: {}", countryRepository.count(), cityRepository.count());
        } catch (IOException e) {
            log.error("Error during data load: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted: " + e.getMessage(), e);
        }
    }

    private Country loadCountry(String countryTitle) {
        Country country = countryRepository.findByTitle(countryTitle);
        return geoMapper.createCountry(country, countryTitle, countryRepository);
    }

    private City loadCity(String cityTitle, Country country, List<City> citiesToSave) {
        City city = cityRepository.findByTitleAndCountry(cityTitle, country);
        return geoMapper.createCity(city, country, cityTitle, citiesToSave);
    }



}
