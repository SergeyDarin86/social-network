package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CaptchaDto {
    @Schema(description = "id под которым капча хранится в бд")
    private String secret;
    @Schema(description = "само изображение")
    private String image;
}
