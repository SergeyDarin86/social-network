package ru.skillbox.diplom.group40.social.network.api.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseDto {
    private UUID id;
    private Boolean isDeleted;
}
