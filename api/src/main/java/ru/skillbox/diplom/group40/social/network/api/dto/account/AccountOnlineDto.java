package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;

@Data
public class AccountOnlineDto extends BaseDto {

    private LocalDateTime lastOnlineTime;

    private Boolean isOnline;
}
