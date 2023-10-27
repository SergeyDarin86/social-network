package ru.skillbox.diplom.group40.social.network.impl.resource.account;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.resource.account.AccountResource;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;

import java.util.UUID;

/**
 * Account
 *
 * @taras281 Taras
 */
@Slf4j
@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountResourceImpl implements AccountResource{

    private final AccountService accountServices;

    @Override
    @GetMapping()
    public ResponseEntity get(@RequestParam String email) {
        log.info("AccountResourceImpl:get() startMethod");
            return ResponseEntity.ok(accountServices.getByEmail(email));
    }

    @Override
    @PutMapping()
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) {
        log.info("AccountResourceImpl:update() startMethod");
            return ResponseEntity.ok(accountServices.update(account));

    }

    @Override
    @PostMapping()
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto account) {
        log.info("AccountResourceImpl:create() startMethod");
            return ResponseEntity.ok(accountServices.create(account));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity getMe() {
        log.info("AccountResourceImpl:getMe() startMethod");
            return ResponseEntity.ok(accountServices.getMe());
    }

    @Override
    public ResponseEntity putMe(@RequestBody AccountDto accountDto) {
        log.info("AccountResourceImpl:putMe() startMethod");
            return ResponseEntity.ok(accountServices.putMe(accountDto));
    }
    @Override
    public ResponseEntity deleteMe() {
        log.info("AccountResourceImpl:deleteMe() startMethod");
            return ResponseEntity.ok(accountServices.delete());
    }

    @Override
    public ResponseEntity getId(@PathVariable UUID id) {
        log.info("AccountResourceImpl:getId() startMethod");
            return ResponseEntity.ok(accountServices.getId(id));
    }

    @Override
    public ResponseEntity deleteId(UUID id){
        log.info("AccountResourceImpl:deleteId() startMethod");
            return ResponseEntity.ok(accountServices.deleteId(id));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) {
        log.info("AccountResourceImpl:getMe() startMethod");
            return ResponseEntity.ok(accountServices.getResultSearch(accountSearchDto, pageable));
    }
}
