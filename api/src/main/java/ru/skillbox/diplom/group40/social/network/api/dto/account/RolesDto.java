package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Getter
@Setter
public class RolesDto {
    private UUID id;
    private String role;

}
