package ru.skillbox.diplom.group40.social.network.api.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;

import java.util.List;
import java.util.UUID;

/**
 * AccountSerchDto
 *
 * @taras281 Taras
 */
@Data
public class AccountSearchDto extends BaseDto {
    @Schema(description = "Список id пользователей запрашиваемых в запросе")
    private List<UUID> ids;
    @Schema(description = "Список id заблокированных пользователей")
    private List<UUID> blockedByIds;
    @Schema(description = "Автор поста")
    private String author;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @Schema(description = "Город проживания")
    private String city;
    @Schema(description = "Страна проживания")
    private String country;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "Заблокирован ли пользователь")
    private Boolean isBlocked;
    @Schema(description = "Статус пользователя для друзей")
    private StatusCode statusCode;
    @Schema(description = "Начало диапазона возрастов пользователей для поискового запроса")
    private Integer ageFrom;
    @Schema(description = "Окончание диапазона возрастов пользователей для поискового запроса")
    private Integer ageTo;
    @Schema(description = "поле для отбора только рекомендованных друзей")
    private Boolean showFriends;
}
