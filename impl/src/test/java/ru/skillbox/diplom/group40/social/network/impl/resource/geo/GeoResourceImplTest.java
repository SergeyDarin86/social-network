package ru.skillbox.diplom.group40.social.network.impl.resource.geo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;
import ru.skillbox.diplom.group40.social.network.impl.security.cookie.JwtTokenWrapper;
import ru.skillbox.diplom.group40.social.network.impl.service.geo.GeoService;
import ru.skillbox.diplom.group40.social.network.impl.utils.feignClient.GeoAdapterClient;
import ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.KafkaListeners;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false, addFilters = false)
class GeoResourceImplTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GeoService geoService;

    @MockBean
    private GeoAdapterClient geoAdapterClient;

    @MockBean
    private JwtTokenWrapper jwtTokenWrapper;

//    @Value("${security.jwt.public-key-pem}")
//    private String token="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu7qaYinf08Q+4Fe0NETTpt7h4019VICO95AbCuFocAOBvoGqXevh/fo5ybSSJwA5sF7NOKyiSBRr7uAl6y6WWFPpwQSR7d+fcbuRX6FV1bx/5y1ZMEDXY3cdw8Pk+U4ZPhtQIsPzT6YtYbYjyhpFqukxXGueaDS4no1kMn8Kq2P5W+ov11lzJn6OaMPF1ndW5aud5EGMn0EgLabVLWXhD6fKGVkrJ8uIAZOmv8EWsiaQcIiUFnIiI+j3CbeaeJSdOcotc+vKdcuSU136rQsIiMTuod2L/raElmPvBkbtKenjZr773/CLOxvaZLdIpPNnxRAxKSgS+b4SFRIRrFr0iwIDAQAB";
//    private String token="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7uppiKd/TxD7gV7Q0RNOm3uHjTX1UgI73kBsK4WhwA4G+gapd6+H9+jnJtJInADmwXs04rKJIFGvu4CXrLpZYU+nBBJHt359xu5FfoVXVvH/nLVkwQNdjdx3Dw+T5Thk+G1Aiw/NPpi1htiPKGkWq6TFca55oNLiejWQyfwqrY/lb6i/XWXMmfo5ow8XWd1blq53kQYyfQSAtptUtZeEPp8oZWSsny4gBk6a/wRayJpBwiJQWciIj6PcJt5p4lJ05yi1z68p1y5JTXfqtCwiIxO6h3Yv+toSWY+8GRu0p6eNmvvvf8Is7G9pkt0ik82fFEDEpKBL5vhIVEhGsWvSLAgMBAAECggEAFninmpYs8EI+oofCZDqpjoEseeh11vkijpi7MUoPwHodpupfidQ+s4enMNwCs1TFaGAYtwQsfW37CNeKQm92WLFcEETrRkBCljsfvi+SQOj08ye2/t3WyoBg2Y+n533FQMLFN2bMR7L6c1v5nGtuU8nhJOZ5n5Cmrs1O+p/BbQgasthGdjYGMSxRmMUc91l7mD72QKWDzYIIQr3HFGFGU8Y/I/jDDGU6GcH5W9n5S9J3s2/bmxBare76N4dYA/H1NW0IhVINlQsEgZqgs1VeR0m0Br6juBc8NMuF63i/7z2L5Zoz2TWS2grvH+qzMhCDpDYqju6C9mpvUJ0iFh5svQKBgQD9OG4kQc8y/AT/QlRLbhxSRHSwIv22mbzNBpqp1VSZiugEsK8s6s1xM8rQ5csUIK7ahIjgauEwRzmG8jTeGF+eQ0Iud21CPjyP/oDPQcaWAOoj/e1v9SymYM1pI8i9Yl6uwyAZogBmIYvdrQcArorSQjEV2PZp9LXtnh9bt/hI9wKBgQC9yiLrjMdPXzLNWd/bucmK0HtF5f7aBLIRQ1xdfB2Guhn/oThZJgPw/l9eCMzWuL5e/wsuioxj1YcztFIk2gQKoK1MF4MOEDdjVv89RT9sd3wBHgof5ZFSBGh4jIV2Hr675z0AurwDBZZSyU4jqR8Wgd8haZSDqam0v+aWJqrADQKBgBC76PN2ceXxp7biwEVX2oFJStjVByVJFaQK+q+cjIG1H04oCvuR7M+9V3rkpL4IxuVj+tOx3H3fRxr26K2AuNjxou8FL3BAQqpVbFfGBgSlvNNyFX3fmvrHzOtU1RBG7vDnrjhjb1lQgDi/FYZjAoOQokPKWcZ4yIkSeQoLp1nTAoGAfoOLiaebiR51uAvsPQV5Ay78rdimbWDp7rYPkUXWTOQrxosLC+pagEChBAcrvQsD5EFxi8HR2g8SBHZ0t5oZTZhUTT/zHeHTekYY+Rf7Mmvn3rrOlvqqR5kmpw1NPjM6g/meYakZAY6s6VzF2OG5FoDWY+iElsv3k8TEil0WP9UCgYEAwAvOZMpldJmOpcjLO4ozjtyAwFQHgyadwx3w5HzQ9toByu1EriYes0wCsRBjUPU2P374QRVIcuMU0cohf+gtczToOjHj1Vy2eZ8JOGXISuDJMA8QJ6w14qTKoniYFQJ15mvEhuqPd+0l4BKYlFiObFSb2IF1rMRuGMjD0Q8hdBQ=";
    @Test
    void load() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer your_auth_token");
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.PUT, new URI("/api/v1/geo/load"));

        // Отправка запроса с RequestEntity
        ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class);
//        ResponseEntity<Void> response = restTemplate.exchange(
//                "/api/v1/geo/load",
//                HttpMethod.PUT,
//                null,
//                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(geoAdapterClient, times(1)).load();
    }

    @Test
    void getCountries() throws Exception{
        List<CountryDto> countries = Arrays.asList(new CountryDto(), new CountryDto());
        when(geoService.getCountries()).thenReturn(countries);
//        when(jwtTokenWrapper.getHeader(any())).thenReturn("qwerty");
//        String token="qwerty";

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        HttpEntity<String> entity= new HttpEntity<>(headers);
//        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI("/api/v1/geo/country"));
//        ResponseEntity<List<CountryDto>> response = restTemplate.exchange(
//                requestEntity,
//                new ParameterizedTypeReference<List<CountryDto>>() {});
        ResponseEntity<List<CountryDto>> response = restTemplate.exchange(
                "/api/v1/geo/country",
                HttpMethod.GET,
//                entity,
                null,
                new ParameterizedTypeReference<List<CountryDto>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countries, response.getBody());
        verify(geoService, times(1)).getCountries();
    }

    @Test
    void getCitiesByCountryId() {
        UUID countryId = UUID.randomUUID();
        List<CityDto> cities = Arrays.asList(new CityDto(), new CityDto());
        when(geoService.getAllCitiesByCountryId(countryId)).thenReturn(cities);

        ResponseEntity<List<CityDto>> response = restTemplate.exchange(
                String.format("/api/v1/geo/country/{countryId}/city", countryId),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CityDto>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cities, response.getBody());
        verify(geoService, times(1)).getAllCitiesByCountryId(countryId);
    }
}