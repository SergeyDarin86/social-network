package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.skillbox.diplom.group40.social.network.api.dto.account.*;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.account.Account_;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.role.RoleService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import javax.security.auth.login.AccountException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private static final String BADREUQEST = "bad reqest";
    private final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;

    private final RoleService roleService;

    public AccountDto create(AccountDto accountDto) throws AccountException {
        log.info("AccountService:create() startMethod");
        Account account = mapperAccount.toEntity(accountDto);
        account.setRegistrationDate(LocalDateTime.now(ZoneId.of("Z")));
        account.setRoles(roleService.getRoleSet(Arrays.asList("USER","MODERATOR")));
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }

    public AccountDto update(AccountDto accountDto) throws AccountException {
        log.info("AccountService:update() startMethod");
        return mapperAccount.toDto(accountRepository.save(mapperAccount.toEntity(accountDto)));
    }

    public AccountDto getByEmail(String email) throws AccountException {
        log.info("AccountService:get(String email) startMethod");
        return mapperAccount.toDto(accountRepository.findFirstByEmail(email).orElseThrow(()->new AccountException("BADREUQEST")));
    }

    public AccountDto getId(UUID uuid) throws AccountException {
        log.info("AccountService:get(String email) startMethod");
        return mapperAccount.toDto(accountRepository.findById(uuid).orElseThrow(()->new AccountException("BADREUQEST")));
    }

    public AccountDto getMe() throws AccountException {
        log.info("AccountService: getMe() startMethod");
        return mapperAccount.toDto(accountRepository.findById(AuthUtil.getUserId()).orElseThrow(()->new AccountException(BADREUQEST)));
    }
    public Page<AccountDto> getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) throws AccountException  {
        Specification spec = like(Account_.COUNTRY, accountSearchDto.getCountry())
                .and(notEqual(Account_.ID, AuthUtil.getUserId()))
                .and(like(Account_.FIRST_NAME, accountSearchDto.getFirstName()))
                .and(like(Account_.LAST_NAME, accountSearchDto.getLastName()))
                .and(like(Account_.CITY, accountSearchDto.getCity()))
                .and(equal(Account_.EMAIL, accountSearchDto.getEmail()))
                .and(between(Account_.BIRTH_DATE, accountSearchDto.getAgeFrom(), accountSearchDto.getAgeTo()))
                .or(in(Account_.ID, accountSearchDto.getIds()));
        if(accountSearchDto.getAuthor()!=null){
            spec = spec.and(like(Account_.FIRST_NAME, accountSearchDto.getAuthor()));
        }
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return accounts.map(mapperAccount::toDto);
    }
    public Page<AccountDto> getAll(AccountSearchDto accountSearchDto, Pageable pageable) throws AccountException  {
        Specification spec = equal(Account_.COUNTRY, accountSearchDto.getCountry())
                .or(like(Account_.FIRST_NAME, accountSearchDto.getFirstName()))
                .or(like(Account_.LAST_NAME, accountSearchDto.getLastName()))
                .or(like(Account_.CITY, accountSearchDto.getCity()))
                .or(equal(Account_.EMAIL, accountSearchDto.getEmail()))
                .or(between(Account_.BIRTH_DATE, accountSearchDto.getAgeFrom(), accountSearchDto.getAgeTo()))
                .or(in(Account_.ID, accountSearchDto.getIds()));
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return accounts.map(mapperAccount::toDto);
    }

    public AccountDto putMe(AccountDto accountDto) throws AccountException{
        log.info("AccountService:putMe() startMethod");
        return mapperAccount.toDto(mapperAccount.rewriteEntity(accountRepository.findById(AuthUtil.getUserId()).get(), accountDto));
    }

    public boolean delete() throws AccountException{
        log.info("AccountService:delete() startMethod");
        accountRepository.deleteById(AuthUtil.getUserId());
        return true;
    }

    public boolean deleteId(UUID id) throws AccountException{
        log.info("AccountService:deleteId() startMethod");
        accountRepository.deleteById(id);
        return true;
    }
    //TODO нужен метод?
    @Deprecated
    public JwtDto getJwtDto(AuthenticateDto authenticateDto) {
        log.info("AccountService:getJwtDto() startMethod");
        Optional<Account> account = accountRepository.findFirstByEmail(authenticateDto.getEmail());
        Assert.isTrue(account.isPresent());
        Assert.isTrue(account.get().getPassword().equals(authenticateDto.getPassword()));
        JwtDto jwtDto = new JwtDto();
        jwtDto.setId(account.get().getId());
        jwtDto.setEmail(account.get().getEmail());
        jwtDto.setRoles(listOfRolesFromSetOfRoles(account.get().getRoles()));
        //Антон ниже строку не убивай мне нужно для записи времени когда был последний раз пользователь
        account.get().setLastOnlineTime(LocalDateTime.now());
        return jwtDto;
    }
    //TODO нужен?
    @Deprecated
    public Boolean doesAccountWithSuchEmailExist(String email){
        return accountRepository.findFirstByEmail(email).isPresent();
    }

    //TODO нужен?
    @Deprecated
    private List<String> listOfRolesFromSetOfRoles(Set<Role> roles) {
        log.info("AccountService:listOfRolesFromSetOfRoles() startMethod");
        ArrayList<String> roleNames = new ArrayList<>();
        for(Role role : roles){
            roleNames.add(role.getRole());
        }
        return roleNames;
    }
    //TODO уже такой метод -> getByEmail
    @Deprecated
    public Account getAccountByEmail(String email){
        return accountRepository.findFirstByEmail(email).orElse(null);
    }

    //TODO нужен?
    @Deprecated
    public void save(Account account){
        accountRepository.save(account);
    }
}
