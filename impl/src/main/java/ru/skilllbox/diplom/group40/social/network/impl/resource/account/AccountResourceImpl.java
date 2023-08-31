package ru.skilllbox.diplom.group40.social.network.impl.resource.account;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skilllbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skilllbox.diplom.group40.social.network.api.resource.account.AccountResource;
import ru.skilllbox.diplom.group40.social.network.impl.service.account.AccountServices;

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
