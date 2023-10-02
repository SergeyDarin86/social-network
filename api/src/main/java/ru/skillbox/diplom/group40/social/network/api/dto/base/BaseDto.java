package ru.skillbox.diplom.group40.social.network.api.dto.base;

import lombok.Data;


import java.io.Serializable;
import java.util.UUID;

@Data
public class BaseDto implements Serializable {
    private UUID id;
    private Boolean isDeleted;
}
