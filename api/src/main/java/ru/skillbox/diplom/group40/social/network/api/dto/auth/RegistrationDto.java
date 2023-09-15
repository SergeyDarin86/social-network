package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password1;

    private String password2;

    private String captchaCode;

//    private String capthaSecret;
}
