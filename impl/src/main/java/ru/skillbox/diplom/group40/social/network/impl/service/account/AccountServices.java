package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.accaunt.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.role.RoleService;

import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountServices {
    private final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;
    private final RoleService roleService;

    public AccountDto save(AccountDto accountDto) throws AccountException {
        checkAccaunt();
        Account account = mapperAccount.toEntity(accountDto);
        Set<Role> roles = roleService.getRoleSet(Arrays.asList("USER","MODERATOR"));
        account.setRoles(roles);
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




    public JwtDto getJwtDto(AuthenticateDto authenticateDto) {
        Account account = accountRepository.findByEmail(authenticateDto.getEmail());
        Assert.isTrue(account != null);
        Assert.isTrue(account.getPassword().equals(authenticateDto.getPassword()));
        JwtDto jwtDto = new JwtDto();
        jwtDto.setEmail(account.getEmail());
        jwtDto.setRoles(listOfRolesFromSetOfRoles(account.getRoles()));
        return jwtDto;
    }

    private List<String> listOfRolesFromSetOfRoles(Set<Role> roles) {
        ArrayList<String> roleNames = new ArrayList<>();
        for(Role role : roles){
            roleNames.add(role.getRole());
        }
        return roleNames;
    }
}
