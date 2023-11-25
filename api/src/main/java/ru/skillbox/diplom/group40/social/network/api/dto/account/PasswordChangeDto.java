package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {
    private String newPassword1;
    private String newPassword2;
}
