package ru.skillbox.diplom.group40.social.network.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password1;

//    private String password2;

//    private String capthaCode;

//    private String capthaSecret;
}
