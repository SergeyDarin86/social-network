package ru.skillbox.diplom.group40.social.network.api.resource.auth;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.ChangeEmailDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.PasswordChangeDto;

@Controller
@RequestMapping("api/v1/auth/")
@Tag(name = "Api сервиса аутентификации",
        description = "Сервис для регистрации, логина, и всего что с этим связано")
public interface AuthController {

    @Operation(summary = "Аутентификация",
            description = "Аутентификация и последующее получение access и refresh токенов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Атентификация прошла упешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticateResponseDto.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Аутентификация не удалась. Не верный логин или пароль",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @Operation(summary = "Регистрация",
            description = "Регистрация")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Регистрация прошла упешно"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Регистрация не удалась. Уже есть пользователь с таким email или неверная капча",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegistrationDto loginDto);

    @Operation(summary = "Обновление токенов",
            description = "Запрос на обновление токенов обновление токенов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Обновление токенов прошло успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticateResponseDto.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверный или неактивный refresh токен",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/refresh")
    ResponseEntity<AuthenticateResponseDto> refresh(@RequestBody RefreshDto refreshDto);

    @Operation(summary = "восстановление пароля (отправка письма)",
            description = "запрос на отправку письма со ссылкой на страницу восстановление пароля")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Письмо на почту отправлено"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Нет пользователя с указанной почтой")
    })
    @PostMapping("/password/recovery/")
    ResponseEntity<String> sendRecoveryEmail(@RequestBody PasswordRecoveryDto recoveryDto);

    @Operation(summary = "восстановление пароля",
            description = "восстановление пароля по ссылке из почты. В качестве ссылки отправлялся айдишник токена " +
                    "восстановления, который добавился в базу при отправке сообщения на почту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "пароль успешно изменён"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверная или просроченная ссылка")
    })
    @PostMapping("/password/recovery/{recoveryTokenId}")
    ResponseEntity<String> changePassword(
            @Parameter(description = "id токена восстановления в бд") @PathVariable String recoveryTokenId,
            @RequestBody NewPasswordDto passwordDto
    );

    @Operation(summary = "выход из системы",
            description = "выход из системы и удаление на бэке токенов из списка активных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Выход из системы успешно выполнен")
    })
    @PostMapping("/logout")
    ResponseEntity<String> logout();

    @Operation(summary = "получение капчи",
            description = "получение капчи. Сопровождается занесением информации о капче в базу.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно получили капчу",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CaptchaDto.class))
                    })
    })
    @GetMapping("/captcha")
    ResponseEntity<CaptchaDto> getCaptcha();

    @Operation(summary = "Получение списка активных пользователей",
            description = "Получение списка активных пользователей основываясь на списках вктивных " +
                    "access и refresh токенов. Ответ идёт в формате одной строки")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получили список активных пользователей")
    })
    @GetMapping("/admin/getActiveUsers")
    ResponseEntity<String> getUsers();

    @Operation(summary = "Отзыв токенов пользователя",
            description = "Отзыв токенов пользователя по его email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access и refresh токены пользователя удалены из списка активных")
    })
    @PostMapping("/admin/revokeUserTokens/{email}")
    ResponseEntity<String> revokeUserTokens(@Parameter(description = "email пользователя")@PathVariable String email);

    @Operation(summary = "Отзыв токенов всех пользователя",
            description = "Полная очистка списков активных токенов пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Все текущие сессии закрыты")
    })
    @PostMapping("/admin/revokeAllTokens")
    ResponseEntity<String> revokeAllTokens();


    @GetMapping("/test")
    ResponseEntity<String> test();

    @Operation(summary = "Изменение пароля",
            description = "Изменение пароля")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пароль успешно изменён",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введённые пароли не совпадают",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/change-password-link")
    ResponseEntity<AccountDto> changePasswordLink(@RequestBody PasswordChangeDto newAggregateEmailDto);

    @Operation(summary = "Изменение почты",
            description = "Изменение почты")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Почта успешно изменена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))
                    })
    })
    @PostMapping("/change-email-link")
    ResponseEntity<AccountDto> changeEmailLink(@RequestBody ChangeEmailDto newAgregatEmailDto);
}
