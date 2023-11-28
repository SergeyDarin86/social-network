package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PasswordRecoveryDto {
    @Schema(description = "емэйл пользователя, доступ к которому надо восстановить")
    private String email;
}
