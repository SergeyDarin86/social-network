package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto {

    @Schema(description = "имя пользователя")
    private String firstName;
    @Schema(description = "фамилия пользователя")
    private String lastName;
    @Schema(description = "почта пользователя")
    private String email;
    @Schema(description = "пароль")
    private String password1;
    @Schema(description = "повторение пароля")
    private String password2;
    @Schema(description = "расшифровка капчи")
    private String captchaCode;
    @Schema(description = "id капчи в бд")
    private String captchaSecret;
}
