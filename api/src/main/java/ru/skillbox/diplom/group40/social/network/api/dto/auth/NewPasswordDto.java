package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewPasswordDto {
    @Schema(description = "новый пароль")
    private String password;
}
