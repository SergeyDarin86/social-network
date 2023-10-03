package ru.skillbox.diplom.group40.social.network.api.resource.account;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountStatisticRequestDto;

import javax.security.auth.login.AccountException;
import java.util.UUID;

/**
 * interfaceAccount
 *
 * @taras281 Taras
 */
@RestController
@RequestMapping("api/v1/account/")
public interface AccountResource {
    @GetMapping()
    public ResponseEntity get(@RequestParam String email) throws AccountException;
    @PutMapping()
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) throws AccountException;
    @PostMapping()
    public ResponseEntity<AccountDto>  create(@RequestBody AccountDto account) throws AccountException;
    @GetMapping("/me")
    public ResponseEntity getMe() throws AccountException;
    @PutMapping("/me")
    public ResponseEntity putMe(@RequestBody AccountDto accountDto) throws AccountException;
    @DeleteMapping("/me")
    public ResponseEntity deleteMe() throws AccountException;

    @GetMapping("/{id}")
    public ResponseEntity getId(@PathVariable UUID id)throws AccountException;
    @DeleteMapping("/{id}")
    public ResponseEntity deleteId(@PathVariable UUID id)throws AccountException;
    @GetMapping("/search")
    public ResponseEntity getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) throws AccountException;

}


