package ru.skilllbox.diplom.group40.social.service.dto;

import lombok.Data;

import java.util.UUID;

/**
 * BaseDto
 *
 * @author Your name
 */
@Data
public class PersonDto {
    private UUID id;
    private Boolean isDeleted;
}
