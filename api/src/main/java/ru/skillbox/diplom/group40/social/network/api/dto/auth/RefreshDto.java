package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshDto {
    @Schema(description = "refresh токен")
    private String refreshToken;
}
