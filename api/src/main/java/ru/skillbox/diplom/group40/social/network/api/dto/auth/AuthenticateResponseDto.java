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
public class AuthenticateResponseDto {
    @Schema(description = "access токен")
    private String accessToken;
    @Schema(description = "refresh токен")
    private String refreshToken;
}