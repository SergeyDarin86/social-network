package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Getter
@Setter
public class Authorities {
    private UUID id;
    private String authority;
}
