package ru.skillbox.diplom.group40.social.network.impl.resource.account;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.api.resource.account.AccountResource;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import javax.security.auth.login.AccountException;
import java.util.UUID;

/**
 * Account
 *
 * @taras281 Taras
 */
@Log4j
@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountResourceImpl implements AccountResource {

    private final AccountService accountServices;

    @Override
    @PostMapping("/")
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto account) {
        log.info("AccountResourceImpl:create() startMethod");
        try {
            return ResponseEntity.ok(accountServices.create(account));
        } catch (AccountException e) {
            return generatorResponse(e);
        }
    }


    @Override
    @PutMapping("/")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) {
        log.info("AccountResourceImpl:update() startMethod");
        try {
            return ResponseEntity.ok(accountServices.update(account));
        } catch (AccountException e) {
            return generatorResponse(e);
        }
    }

    @Override
    @GetMapping("/")
    public ResponseEntity get(@RequestParam String email) {
        log.info("AccountResourceImpl:get() startMethod");
        try {
            return ResponseEntity.ok(accountServices.get(email));
        } catch (AccountException e) {
            return generatorResponse(e);
        }
    }


    @Override
    @GetMapping("/me")
    public ResponseEntity getMe() {
        log.info("AccountResourceImpl:getMe() startMethod");
        try {
            return ResponseEntity.ok(accountServices.getMe());
        } catch (AccountException e) {
            return generatorResponse(e);
        }
    }


    private ResponseEntity generatorResponse(AccountException e) {
        log.info("AccountResourceImpl:generateResponse() startMethod");
        if(e.getMessage().equals("unautorized")){
            return ResponseEntity.status(401).body("Unauthorized");}
        return ResponseEntity.status(400).body("Bad request");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("AccountResourceImpl:test() startMethod");
        JwtDto jwtDto = AuthUtil.getJwtDto();
        UUID userId = AuthUtil.getUserId();
        System.out.println(jwtDto);
        System.out.println(userId);
        return ResponseEntity.ok("hello from test method");
    }
}
