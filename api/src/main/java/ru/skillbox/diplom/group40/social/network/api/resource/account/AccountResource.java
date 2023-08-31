package ru.skillbox.diplom.group40.social.network.api.resource.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;

import javax.security.auth.login.AccountException;

/**
 * interfaceAccount
 *
 * @taras281 Taras
 */
@RestController
@RequestMapping("api/v1/account/")
public interface AccountResource {

    @PostMapping("/")
    public ResponseEntity<AccountDto>  create(@RequestBody AccountDto account) throws AccountException;

    @PutMapping("/")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) throws AccountException;

    @GetMapping("/")
    public ResponseEntity get(@RequestParam String authorization, @RequestParam String email) throws AccountException;
    @GetMapping("/me")
    public ResponseEntity getMe(@RequestParam String authorization) throws AccountException;
}


