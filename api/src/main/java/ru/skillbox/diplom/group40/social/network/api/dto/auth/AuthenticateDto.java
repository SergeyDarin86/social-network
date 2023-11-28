package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateDto{
    @Schema(description = "email пользователя")
    private String email;
    @Schema(description = "пароль пользователя")
    private String password;
}
