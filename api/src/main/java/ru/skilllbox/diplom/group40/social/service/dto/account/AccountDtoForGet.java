package ru.skilllbox.diplom.group40.social.service.dto.account;

import lombok.Data;

import java.util.UUID;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Data
public class AccountDtoForGet {
    private UUID id;
    private boolean isDeleted;
    private String firstName;
    private String email;
    private String password;
    private RolesDto roles;
    private Authorities authorities;
}
