package ru.skillbox.diplom.group40.social.network.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto extends BaseDto {

    @Schema(description = "имя пользователя")
    private String firstName;
    @Schema(description = "вамилия пользователя")
    private String lastName;
    @Schema(description = "почта пользователя")
    private String email;
    @Schema(description = "время регистрации")
    private LocalDateTime registrationDate;
    @Schema(description = "время создания аккаунта")
    private LocalDateTime createdOn;
    @Schema(description = "время последнего изменения")
    private LocalDateTime updatedOn;
    @Schema(description = "захешированный пароль пользователя")
    private String password;
}
