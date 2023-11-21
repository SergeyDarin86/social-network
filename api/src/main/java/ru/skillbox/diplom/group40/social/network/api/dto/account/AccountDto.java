package ru.skillbox.diplom.group40.social.network.api.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Data
public class AccountDto extends BaseDto {
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "пароль")
    private String password;
    @Schema(description = "Телефон")
    private String phone;
    @Schema(description = "Ссылка на фото пользователя")
    private String photo;
    @Schema(description = "Ссылка на фото для фона аватара")
    private String profileCover;
    @Schema(description = "О себе")
    private String about;
    @Schema(description = "Город проживания")
    private String city;
    @Schema(description = "Страна проживания")
    private String country;
    @Schema(description = "Время и дата регистрации")
    private LocalDateTime regDate;
    @Schema(description = "Статус пользователя для друзей")
    private StatusCode statusCode;
    @Schema(description = "День рождения")
    private LocalDateTime birthDate;
    @Schema(description = "Сообщение")
    private String messagePermission;
    @Schema(description = "Время и дата последней активности пользователя в социальной сети")
    private LocalDateTime lastOnlineTime;
    @Schema(description = "В сети пользователь или нет")
    private Boolean isOnline;
    @Schema(description = "Заблокирован ли пользователь")
    private Boolean isBlocked;
    private String emojiStatus;
    @Schema(description = "Время удаления пользователя")
    private LocalDateTime deletionTimestamp;
    @Schema(description = "Время и дата регистрации в сети пользователя")
    private LocalDateTime createdDate;
    @Schema(description = "Время и дата изменения личных данных пользователяпользователя")
    private LocalDateTime lastModifiedDate;

}
