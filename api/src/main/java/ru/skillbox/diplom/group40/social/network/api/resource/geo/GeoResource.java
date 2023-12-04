package ru.skillbox.diplom.group40.social.network.api.resource.geo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CityDto;
import ru.skillbox.diplom.group40.social.network.api.dto.geo.CountryDto;


import java.util.List;
import java.util.UUID;


@Tag(name = "Api гео-сервиса соцсети",
        description = "Сервис для установки геоданных пользователей соцсети: страна, город (населенный пункт и т.п.)")
@RequestMapping("api/v1/geo")
public interface GeoResource {

    @Operation(summary = "Загрузка списка стран и городов с внешнего ресурса",
            description = "Загружает список стран и городов при помощи Гео-Адаптера")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CountryDto.class)))
                    }
                    ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/load")
    void load() throws Exception;

    @Operation(summary = "Получение списка стран",
            description = "Получает записи всех стран")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен. Страна найдена.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CountryDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/country")
    ResponseEntity <List<CountryDto>> getCountries();


    @Operation(summary = "Получение списка городов",
            description = "Получение списка городов по id страны")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен. Город найден.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CityDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/country/{countryId}/city")
    ResponseEntity <List<CityDto>> getCitiesByCountryId(@PathVariable UUID countryId);


}
