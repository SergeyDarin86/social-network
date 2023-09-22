package ru.skillbox.diplom.group40.social.network.impl.service.geo;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.domain.geo.City;
import ru.skillbox.diplom.group40.social.network.domain.geo.Country;
import ru.skillbox.diplom.group40.social.network.impl.mapper.geo.GeoMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CityRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.geo.CountryRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GeoMapper geoMapper;
    private static final String PATHFILE = "impl/src/main/resources/geoData/worldcities.csv";

    public List<CountryDto> getCountries() {
        log.info("Start method getCountries: ");
        List<Country> countryList = countryRepository.findAll();
        List<CountryDto> countryDtoList = geoMapper.countryToDto(countryList);
        log.info("Количество стран: {}", countryDtoList.size());
        return countryDtoList.stream()
                .sorted(Comparator.comparing(CountryDto::getTitle))
                .collect(Collectors.toList());
    }

    public List<CityDto> getAllCitiesByCountryId(UUID id) {
        log.info("Страна с id= {}", id);
        List<City> cityList = cityRepository.findByCountryId(id);
        List<CityDto> cityDtoList = geoMapper.cityToDto(cityList);
        log.info("Найдено городов в стране с id= {}: {} ", id, cityDtoList.size());
        return cityDtoList.stream()
                .sorted(Comparator.comparing(CityDto::getTitle))
                .collect(Collectors.toList());
    }

    public boolean isDataEmpty() {
        return countryRepository.count() == 0 || cityRepository.count() == 0;
    }

    public void load() {
        Map<String, Country> countryMap = new ConcurrentHashMap<>();
        List<City> citiesToSave = Collections.synchronizedList(new ArrayList<>());
        loadGeo(countryMap, citiesToSave);
        log.info("Количество стран: {} , Количество городов: {}", countryRepository.count(), cityRepository.count());
    }

    private void loadGeo(Map<String, Country> countryMap, List<City> citiesToSave) {
        try (CSVReader reader = new CSVReader(new FileReader(PATHFILE))) {
            List<String[]> areas = reader.readAll().stream().skip(1).collect(Collectors.toList());
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (String[] area : areas) {
                executorService.submit(() -> {
                    String cityTitle = area[0];
                    String countryTitle = area[4];
                    Country country = countryMap.computeIfAbsent(countryTitle, this::loadCountry);
                    City city = loadCity(cityTitle, country, citiesToSave);
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (!citiesToSave.isEmpty()) {
                cityRepository.saveAll(citiesToSave);
            }
        } catch (IOException | CsvException e) {
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
