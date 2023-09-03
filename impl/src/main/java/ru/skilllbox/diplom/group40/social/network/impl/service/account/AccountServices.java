package ru.skilllbox.diplom.group40.social.network.impl.service.account;

import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skilllbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skilllbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skilllbox.diplom.group40.social.network.domain.account.Account;
import ru.skilllbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skilllbox.diplom.group40.social.network.impl.repository.accaunt.AccountRepository;

import javax.security.auth.login.AccountException;

@Service
@RequiredArgsConstructor
public class AccountServices {
    private final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;

    public AccountDto save(AccountDto accountDto) throws AccountException {
        checkAccaunt();
        Account account = mapperAccount.toEntity(accountDto);
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }

    public AccountDto update(AccountDto accountDto) throws AccountException {
        checkAccaunt();
        Account account = accountRepository.findById(accountDto.getId()).get();
        account = mapperAccount.toEntity(accountDto);
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }

    private AccountDto getEtityAccount(Account account) {
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }

    public AccountDtoForGet get(String authorization, String email) throws AccountException {
        checkAccaunt();
        Account account = accountRepository.findByEmail(email);
        AccountDtoForGet accountDtoForGet = mapperAccount.toDtoForGet(account);
        return accountDtoForGet;
    }
    public AccountDto getMe(String authorization) throws AccountException {
        Account account= accountRepository.findFirstByEmail(authorization);
        AccountDto accountDto = mapperAccount.toDto(account);
        return accountDto;
    }

    private void checkAccaunt() throws AccountException {
        if(!getUnauthorized()){
            throw new AccountException("unautorized");
        }
        if(!getCorrectAccountDto()){
            throw new AccountException("badreq");
        }
    }

    private boolean getCorrectAccountDto() {
        return true;
    }
    private boolean getUnauthorized() {
        return true;
    }


}
