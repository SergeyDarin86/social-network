package ru.skilllbox.diplom.group40.social.service.resource.account;


import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skilllbox.diplom.group40.social.service.dto.account.AccountDto;
import ru.skilllbox.diplom.group40.social.service.service.account.AccountServices;

/**
 * Account
 *
 * @taras281 Taras
 */
@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountResourceImpl implements AccountResource {

    private final AccountServices accountServices;

    @Override
    @PostMapping("/")
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto account) {
        return accountServices.save(account);
    }

    @Override
    @PutMapping("/")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account) {
        return accountServices.update(account);
    }

    @Override
    @GetMapping("/")
    public ResponseEntity get(@RequestParam String authorization, @RequestParam String email) {
        return accountServices.get(authorization, email);
    }
}
