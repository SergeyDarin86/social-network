package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class AccountStatisticRequestDto {
    private LocalDateTime date;
    private LocalDateTime firstMonth;
    private LocalDateTime lastMonth;
}
