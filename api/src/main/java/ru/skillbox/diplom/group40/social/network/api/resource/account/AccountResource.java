package ru.skillbox.diplom.group40.social.network.api.resource.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;

import javax.security.auth.login.AccountException;
import java.util.UUID;

/**
 * interfaceAccount
 *
 * @taras281 Taras
 */
@RestController
@RequestMapping("api/v1/account/")
@Tag(name = "Api сервиса аккаунта",
        description = "Сервис для создания, редактирования, получения, удаления аккаунта")
public interface AccountResource {

    @Operation(summary = "Получение данных акаунта по email",
            description = "Получение данных акаунта по email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    public ResponseEntity get(@RequestParam String email) throws AccountException;

    @Operation(summary = "Обновление данных аккаунта",
            description = "Обновление данных аккаунта ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    @PutMapping()
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) throws AccountException;

    @Operation(summary = "Создание акаунта",
            description = "Создание акаунта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    @PostMapping()
    public ResponseEntity<AccountDto>  create(@RequestBody AccountDto account) throws AccountException;
    @Operation(summary = "Получение данных акаунта",
            description = "Получение данных акаунта текущего пользователя. " +
                    "<br>Для всех методов сервиса  Accaunt Вам надо реализовать интерфейс контроллера, контроллер, и сам сервис. <br> Общая работа подразумевает:" +
                    "<br>контроллер принимает в теле запроса параметры и передает их в сервис," +
                    "<br>Сервис выполняет логику и возвращает в контроллер DTO" +
                    "<br>Контроллер оборачивает ответ сервиса в ResponseEntity" +
                    "<br>Для работы со временем используйте тип ZonaDateTime" +
                    "<br>для работы c boolean используйте Boolean")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    @GetMapping("/me")
    public ResponseEntity getMe() throws AccountException;
    @Operation(summary = "Обновление данных акаунта",
            description = "Обновление данных акаунта текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    @PutMapping("/me")
    public ResponseEntity putMe(@RequestBody AccountDto accountDto) throws AccountException;

    @Operation(summary = "Удаление аккаунта",
            description = "Удаление аккаунта авторизированного(текущего) пользователя, ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))
                    }
                    ),
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
    @DeleteMapping("/me")
    public ResponseEntity deleteMe() throws AccountException;


    @Operation(summary = "Получение данных аккаунта по ID",
            description = "Получение данных аккаунта по ID тип-UUID, ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
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
    @GetMapping("/{id}")
    public ResponseEntity getId(@PathVariable UUID id)throws AccountException;

    @Operation(summary = "Удаление аккаунта по ID",
            description = "Удалениеаккаунта по ID тип-UUID, ")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class))
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
    @DeleteMapping("/{id}")
    public ResponseEntity deleteId(@PathVariable UUID id)throws AccountException;

    @Operation(summary = "Поиск аккаунта(ов) по заданным данным",
            description = "Поиск аккаунта(ов) по заданным данным в AccountSearchDto, Pagable -  структура данных которую формирует и передает фронт, для нее DTO не надо делать. Метод должен вернуть объект типа Page<AccountDto>" +
                    "<br> Поле showFriends нужно для отображения друзей текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))
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
    @GetMapping("/search")
    public ResponseEntity getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) throws AccountException;

}


