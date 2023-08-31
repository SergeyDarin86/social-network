package ru.skilllbox.diplom.group40.social.service.resource.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skilllbox.diplom.group40.social.service.dto.account.AccountDto;
/**
 * interfaceAccount
 *
 * @taras281 Taras
 */
@RestController
@RequestMapping("api/v1/account/")
public interface AccountResource {

    @PostMapping("/")
    public ResponseEntity<AccountDto>  create(@RequestBody AccountDto account);

    @PutMapping("/")
    public ResponseEntity<AccountDto> update(@RequestBody AccountDto account);

    @GetMapping("/")
    public ResponseEntity get(@RequestParam String authorization, @RequestParam String emai);
}


