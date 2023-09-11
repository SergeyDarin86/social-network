package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
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
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import javax.security.auth.login.AccountException;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        account.setCreatedOn(LocalDateTime.now(ZoneId.of("Z")));
        account.setRegistrationDate(LocalDateTime.now(ZoneId.of("Z")));
        Set<Role> roles = roleService.getRoleSet(Arrays.asList("USER","MODERATOR"));
        account.setRoles(roles);
        account = accountRepository.save(account);
        log.info("AccountService:create() endMethod");
        return mapperAccount.toDto(account);
    }

    @Transactional
    public AccountDto update(AccountDto accountDto) throws AccountException {
        log.info("AccountService:update() startMethod");
        Account account = mapperAccount.toEntity(accountDto);
        account.setUpdatedOn(LocalDateTime.now(ZoneId.of("Z")));
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }



    @Transactional
    public AccountDto get(String email) throws AccountException {
        log.info("AccountService:get(String email) startMethod");
        return mapperAccount.toDto(accountRepository.findFirstByEmail(email).orElseThrow(()->new AccountException("BADREUQEST")));
    }
    @Transactional
    public AccountDto getId(UUID uuid) throws AccountException {
        log.info("AccountService:get(String email) startMethod");
        return mapperAccount.toDto(accountRepository.findById(uuid).orElseThrow(()->new AccountException("BADREUQEST")));
    }
    @Transactional
    public AccountDto getMe() throws AccountException {
        log.info("AccountService:getMe() startMethod");
        UUID userId = AuthUtil.getUserId();
        return mapperAccount.toDto(accountRepository.findById(userId).orElseThrow(()->new AccountException(BADREUQEST)));
    }
    @Transactional
    public JwtDto getJwtDto(AuthenticateDto authenticateDto) {
        log.info("AccountService:getJwtDto() startMethod");
        Optional<Account> account = accountRepository.findFirstByEmail(authenticateDto.getEmail());
        Assert.isTrue(account.isPresent());
        Assert.isTrue(account.get().getPassword().equals(authenticateDto.getPassword()));
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
        log.info("AccountService:listOfRolesFromSetOfRoles() startMethod");
        ArrayList<String> roleNames = new ArrayList<>();
        for(Role role : roles){
            roleNames.add(role.getRole());
        }
        return roleNames;
    }

    public Page<AccountDto> getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) throws AccountException  {
        Specification spec = SpecificationUtils
                                               .like(Account_.COUNTRY, accountSearchDto.getCountry())
                                               .and(SpecificationUtils.like(Account_.FIRST_NAME, accountSearchDto.getFirstName()))
                                               .and(SpecificationUtils.like(Account_.LAST_NAME, accountSearchDto.getLastName()))
                                               .and(SpecificationUtils.like(Account_.CITY, accountSearchDto.getCity()))
                                               .and(SpecificationUtils.ageTo(Account_.BIRTH_DATE, accountSearchDto.getAgeTo()))
                                               .and(SpecificationUtils.equal(Account_.EMAIL, accountSearchDto.getEmail()))
                                               .and(SpecificationUtils.ageFrom(Account_.BIRTH_DATE, accountSearchDto.getAgeFrom()))
                                               .or(SpecificationUtils.equalIn(Account_.ID, accountSearchDto.getIds()));
        if(accountSearchDto.getAuthor()!=null){
            spec = spec.and(SpecificationUtils.like(Account_.FIRST_NAME, accountSearchDto.getAuthor()));
        }
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        return accounts.map(mapperAccount::toDto);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findFirstByEmail(email).orElse(null);
    }

    public void save(Account account){
        accountRepository.save(account);
    }

    public AccountDto putMe(AccountDto accountDto) throws AccountException{
        log.info("AccountService:putMe() startMethod");
        UUID userId = AuthUtil.getUserId();
        Account account = accountRepository.findById(userId).get();
        account=rewrite(account, accountDto);
        account.setUpdatedOn(LocalDateTime.now(ZoneId.of("Z")));
        account = accountRepository.save(account);
        return mapperAccount.toDto(account);
    }
    private Account rewrite(Account account, AccountDto accountDto) {
        if(!accountDto.getAbout().isEmpty()){
            account.setAbout(accountDto.getAbout());
        }
        if(!accountDto.getPhone().isEmpty()){
            account.setPhone(accountDto.getPhone());
        }
        if(!accountDto.getBirthDate().equals(null)){
            account.setBirthDate(accountDto.getBirthDate());
        }
        if(!accountDto.getCity().isEmpty()){
            account.setCity(accountDto.getCity());
        }
        if(!accountDto.getCountry().isEmpty()){
            account.setCountry(accountDto.getCountry());
        }
        if(!accountDto.getFirstName().isEmpty()){
            account.setFirstName(accountDto.getFirstName());
        }
        if(!accountDto.getLastName().isEmpty()){
            account.setLastName(accountDto.getLastName());
        }
        return account;
    }

    public boolean delete() throws AccountException{
        UUID userId = AuthUtil.getUserId();
        accountRepository.deleteById(userId);
        return true;
    }

    public boolean deleteId(UUID id) throws AccountException{
        log.info("AccountService:deleteId() startMethod");
        accountRepository.deleteById(id);
        return true;
    }

    public Object getStatistic(AccountStatisticRequestDto accountStatisticRequestDto) throws AccountException{
        log.info("AccountService:deleteId() startMethod");
        Specification spec =
                SpecificationUtils.ageFromDate(Account_.BIRTH_DATE, accountStatisticRequestDto.getDate())
                .and(SpecificationUtils.ageFromDate(Account_.CREATED_ON, accountStatisticRequestDto.getFirstMonth()))
                .and(SpecificationUtils.ageToDate(Account_.CREATED_ON, accountStatisticRequestDto.getLastMonth()));
                //
        List<Account> accounts = accountRepository.findAll(spec);
    return null;
    }
}
