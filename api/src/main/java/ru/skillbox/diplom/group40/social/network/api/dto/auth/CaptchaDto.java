package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import lombok.Data;

@Data
public class CaptchaDto {
    private String secret;
    private String image;
}
