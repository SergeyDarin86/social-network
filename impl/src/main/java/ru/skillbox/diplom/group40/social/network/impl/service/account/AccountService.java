package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.role.RoleService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import javax.security.auth.login.AccountException;
import java.util.*;
@Log4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private static final String BADREUQEST = "bad reqest";
    private final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;
    private final RoleService roleService;

    @Transactional
    public AccountDto create(AccountDto accountDto) throws AccountException {
        log.info("AccountService:create() startMethod");
        Account account = mapperAccount.toEntity(accountDto);
        Set<Role> roles = roleService.getRoleSet(Arrays.asList("USER","MODERATOR"));
        account.setRoles(roles);
        account = accountRepository.save(account);
        log.info("AccountService:create() endMethod");
        return mapperAccount.toDto(account);
    }

    @Transactional
    public AccountDto update(AccountDto accountDto) throws AccountException {
        log.info("AccountService:update() startMethod");
        UUID userId = AuthUtil.getUserId();
        Account account = accountRepository.findById(accountDto.getId()).get();
        account = mapperAccount.toEntity(accountDto);
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }
    @Transactional
    public AccountDtoForGet get(String email) throws AccountException {
        log.info("AccountService:get(String email) startMethod");
        UUID userId = AuthUtil.getUserId();
        if(userId.equals(null)){
            throw new AccountException(BADREUQEST);
        }
        Optional<List<Account>> account = accountRepository.findByEmail(email);
        if(account.get().size()!=1){
            throw new AccountException(BADREUQEST);
        }
        AccountDtoForGet accountDtoForGet = mapperAccount.toDtoForGet(account.get().get(0));
        return accountDtoForGet;
    }
    @Transactional
    public AccountDto getMe() throws AccountException {
        log.info("AccountService:getMe() startMethod");
        UUID userId = AuthUtil.getUserId();
        if(userId.equals(null)){
            throw new AccountException(BADREUQEST);
        }
        Optional<Account> account= accountRepository.findById(userId);
        if(account.isEmpty()){
            throw new AccountException(BADREUQEST);
        }
        AccountDto accountDto = mapperAccount.toDto(account.get());
        return accountDto;
    }
    @Transactional
    public JwtDto getJwtDto(AuthenticateDto authenticateDto) {
        log.info("AccountService:getJwtDto() startMethod");
        Optional<Account> account = accountRepository.findFirstByEmail(authenticateDto.getEmail());
        if(account.isEmpty()){return null;}
        if(!account.get().getPassword().equals(authenticateDto.getPassword())){return null;}
        JwtDto jwtDto = new JwtDto();
        jwtDto.setId(account.get().getId());
        jwtDto.setEmail(account.get().getEmail());
        jwtDto.setRoles(listOfRolesFromSetOfRoles(account.get().getRoles()));
        return jwtDto;
    }

    @Transactional
    public Boolean doesAccountWithSuchEmailExist(String email){
        return accountRepository.findFirstByEmail(email).isPresent();
    }

    private List<String> listOfRolesFromSetOfRoles(Set<Role> roles) {
        log.info("AccountService:getJwtDto() startMethod");
        ArrayList<String> roleNames = new ArrayList<>();
        for(Role role : roles){
            roleNames.add(role.getRole());
        }
        return roleNames;
    }
}
