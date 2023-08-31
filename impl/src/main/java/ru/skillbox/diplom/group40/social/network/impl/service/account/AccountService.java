package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    public static final String BADREQEST = "Bad request";
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
        Optional<List<Account>> account = accountRepository.findByEmail(email);
        if(account.get().size()!=1){
            throw new AccountException(BADREQEST);}
        AccountDtoForGet accountDtoForGet = mapperAccount.toDtoForGet(account.get().get(0));
        return accountDtoForGet;
    }
    public AccountDto getMe(String authorization) throws AccountException {
        checkAccaunt();
        Optional<List<Account>> account = accountRepository.findByEmail(authorization);
        if(account.get().size()!=1){
            throw new AccountException(BADREQEST);}
        AccountDto accountDto= mapperAccount.toDto(account.get().get(0));
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
