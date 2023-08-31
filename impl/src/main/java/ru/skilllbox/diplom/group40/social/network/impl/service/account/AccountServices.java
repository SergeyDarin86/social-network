package ru.skilllbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skilllbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skilllbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skilllbox.diplom.group40.social.network.domain.account.Account;
import ru.skilllbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skilllbox.diplom.group40.social.network.impl.repository.accaunt.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountServices {
    private final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;

    public ResponseEntity<AccountDto> save(AccountDto accountDto) {
        if(!getUnauthorized()){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        if(!getCorrectAccountDto()){
            return  ResponseEntity.badRequest().build();
        }
        Account account = mapperAccount.toEntity(accountDto);
        account = accountRepository.save(account);
        return ResponseEntity.ok(mapperAccount.toDto(account));
    }

    public ResponseEntity<AccountDto> update(AccountDto accountDto) {
        if(!getUnauthorized()){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        if(!getCorrectAccountDto()){
            return  ResponseEntity.badRequest().build();
        }
        Account account = accountRepository.findById(accountDto.getId()).get();
        account = mapperAccount.toEntity(accountDto);
        account = accountRepository.save(account);
        return ResponseEntity.ok(mapperAccount.toDto(account));
    }

    private AccountDto getEtityAccount(Account account) {
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }

    public ResponseEntity<AccountDtoForGet> get(String authorization, String email) {
        if(!getUnauthorized()){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        if(!getCorrectAccountDto()){
            return  ResponseEntity.badRequest().build();
        }
        Account account = accountRepository.findByEmail(email);
        AccountDtoForGet accountDtoForGet = mapperAccount.toDtoForGet(account);
        return ResponseEntity.ok(accountDtoForGet);
    }
    private boolean getCorrectAccountDto() {
        return true;
    }
    private boolean getUnauthorized() {
        return true;
    }
}
