package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {
    @Schema(description = "id токена")
    private String tokenId;
    @Schema(description = "id пользователя")
    private String userId;
    @Schema(description = "email пользователя")
    private String email;
    @Schema(description = "список ролей пользователя")
    private List<String> roles;
}
