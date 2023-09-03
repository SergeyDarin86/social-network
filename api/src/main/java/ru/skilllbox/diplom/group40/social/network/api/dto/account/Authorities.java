package ru.skilllbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;

import java.util.UUID;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Data
public class Authorities {
    private UUID id;
    private String authority;
}
