package ru.skillbox.diplom.group40.social.network.api.resource.dialogs.friend;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendCountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Tag(name = "Api сервиса дружбы пользователей соцсети",
        description = "Сервис для установки связей между пользователями соцсети: дружба, подписка, блокирование, разблокирование и др.")
@RequestMapping("api/v1/friends")
public interface FriendResource {


    @Operation(summary = "Отправка запроса на дружбу по id",
            description = "Отправка запроса на дружбу текущем пользователем по id другого пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FriendDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/{id}/request")
    ResponseEntity<FriendDto> create(
            @Parameter(description = "Идентификатор пользователя для запроса на дружбу") @PathVariable UUID id);

    @Operation(summary = "Одобрение запроса на дружбу по id",
            description = "Одобрение запроса на дружбу текущем пользователем по id другого пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FriendDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Запись по id не найдена.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/{id}/approve")
    ResponseEntity<FriendDto> approve(
            @Parameter(description = "Идентификатор пользователя для одобрения дружбы") @PathVariable UUID id);

    @Operation(summary = "Удаление связи между пользователями",
            description = "Мягкое удаление записей взаимосвязи текущего пользователя по id записи другого пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен. Запись удалена.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Запись по id не найдена.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(
            @Parameter(description = "Идентификатор пользователя для удаления отношений") @PathVariable UUID id);

    @Operation(summary = "Получает связи пользователя по входящим парамтрам отбора",
            description = "Поиск связей пользователя с учетом различных критериев отбора")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен. Запись найдена.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FriendDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping()
    ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto friendSearchDto, Pageable page);

    @Operation(summary = "Получение записи об отношениях пользователей по id",
            description = "Получает запись взаимосвязи пользователей по id записи")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен. Запись найдена.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Запись по id не найдена.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/{id}")
    ResponseEntity<FriendDto> getById(
            @Parameter(description = "Идентификатор записи отношений") @PathVariable UUID id);

    @Operation(summary = "Получение пользователей по статусу отношений",
            description = "Получает список UUID пользователей находящихся в отношениях с текущим пользователем " +
                    "по статусу отношений (дружба, подписка, блокировка, и т.д)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/status/{status}")
    ResponseEntity<List<String>> getByStatus(
            @Parameter(description = "Статус отношений для отбора") @PathVariable StatusCode status);

    @Operation(summary = "Получение рекомендаций дружбы для текущего пользователя",
            description = "Получает список пользователей рекомендованных для дружбы")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FriendDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/recommendations")
    ResponseEntity<List<FriendDto>> recommendations();

    @Operation(summary = "Установка блокировки по id",
            description = "Блокирует отношения текущего пользователя по id блокируемого пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/block/{id}")
    ResponseEntity<FriendDto> block(
            @Parameter(description = "Идентификатор пользователя для блокировки") @PathVariable UUID id);

    @Operation(summary = "Удаление блокировки по id",
            description = "Разбокировка отношений текущего пользователя по id заблокированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Запись по id не найдена.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/unblock/{id}")
    ResponseEntity<FriendDto> unblock(
            @Parameter(description = "Идентификатор пользователя для разблокировки") @PathVariable UUID id);

    @Operation(summary = "Установка подписки по id",
            description = "Установка подписки текущего пользователя на другого пользователя по его id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/subscribe/{id}")
    ResponseEntity<FriendDto> subscribe(
            @Parameter(description = "Идентификатор пользователя для подписки") @PathVariable UUID id);

    @Operation(summary = "Получение количества запросов в друзья для текущего пользователя",
            description = "Возвращает число входящих запросов в друзья для текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FriendCountDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/count")
    ResponseEntity<FriendCountDto> count();

    @Operation(summary = "Получение списока id всех друзей текущего пользователя",
            description = "Получение списока id всех друзей текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/friendId")
    ResponseEntity<List<String>> getAllFriendsId();

    @Operation(summary = "Получение друзей пользователя по id",
            description = "Получение списока id всех друзей пользователя по его id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/friendId/{id}")
    ResponseEntity<List<String>> getAllFriendsIdById(
            @Parameter(description = "Идентификатор пользователя для поиска его друзей") @PathVariable UUID id);

    @Operation(summary = "Получение всех связей текущего пользователя",
            description = "Возвращает список id со статусоми отношений для всех связей текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/check{ids}")
    ResponseEntity<Map<String, String>> getFriendsStatus(FriendSearchDto friendSearchDto);

    @Operation(summary = "Получение заблокированных связей текущего пользователя",
            description = "Возвращает список id всех заблокированных связей текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к данным запрещен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Неизвестная ошибка.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/blockFriendId")
    ResponseEntity<List<String>> getAllBlocked();

}
