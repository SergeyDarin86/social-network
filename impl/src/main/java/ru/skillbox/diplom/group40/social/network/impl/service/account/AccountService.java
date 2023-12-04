package ru.skillbox.diplom.group40.social.network.impl.service.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.account.*;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.account.Account_;
import ru.skillbox.diplom.group40.social.network.impl.exception.AccountException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.role.RoleService;
import ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation.Logging;
import ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation.Metric;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.*;

@Slf4j
@Metric
@Getter
@Service
@Transactional
@EnableScheduling
@RequiredArgsConstructor
public class AccountService {
    private static final String BADREUQEST = "bad reqest";
    public final MapperAccount mapperAccount;
    private final AccountRepository accountRepository;
    private final FriendService friendService;
    private final NotificationSettingsService notificationSettingsService;

    private final RoleService roleService;

    private final JwtEncoder accessTokenEncoder;

    private final KafkaService kafkaService;
    private final PasswordEncoder passwordEncoder;

    @Logging
    public AccountDto create(AccountDto accountDto) {
        log.info("AccountService:create() startMethod");
        getErrorIfNull(accountDto);
        Account account = mapperAccount.toEntity(accountDto);
        account.setRegistrationDate(LocalDateTime.now(ZoneId.of("Z")));
        account.setRoles(roleService.getRoleSet(Arrays.asList("USER", "MODERATOR")));
        account = accountRepository.save(account);
        notificationSettingsService.createSettings(account.getId());
        return mapperAccount.toDto(account);
    }

    @Logging
    public AccountDto update(AccountDto accountDto) {
        log.info("AccountService:update() startMethod");
        getErrorIfNull(accountDto);
        log.info("AccountService:putMe() startMethod");
        Account account = accountDto.getId() != null ? accountRepository.findById(accountDto.getId()).get() : accountRepository.findById(AuthUtil.getUserId()).get();
        account = mapperAccount.rewriteEntity(accountRepository.findById(account.getId()).get(), accountDto);
        accountRepository.save(account);
        return mapperAccount.toDto(account);
    }


    @Logging
    public AccountDto getByEmail(String email) {
        log.info("AccountService:get(String email) startMethod");
        getErrorIfNull(email);
        return mapperAccount.toDto(accountRepository.findFirstByEmail(email).orElseThrow(() -> new AccountException("BADREUQEST")));
    }

    @Logging
    public AccountDto getId(UUID uuid) {
        log.info("AccountService:get(String email) startMethod");
        getErrorIfNull(uuid);
        return mapperAccount.toDto(accountRepository.findById(uuid).orElseThrow(() -> new AccountException("BADREUQEST")));
    }

    @Logging
    public AccountDto getMe() {
        log.info("AccountService: getMe() startMethod");
        return mapperAccount.toDto(accountRepository.findById(AuthUtil.getUserId()).orElseThrow(() -> new AccountException(BADREUQEST)));
    }

    @Logging
    public boolean delete() {
        log.info("AccountService:delete() startMethod");
        accountRepository.deleteById(AuthUtil.getUserId());
        return true;
    }

    @Logging
    public boolean deleteById(UUID id) {
        log.info("AccountService:deleteId() startMethod");
        getErrorIfNull(id);
        accountRepository.deleteById(id);
        return true;
    }

    @Logging
    public Page<AccountDto> getResultSearch(AccountSearchDto accountSearchDto, Pageable pageable) {
        SecurityContext sc = SecurityContextHolder.getContext();
        log.info("AccountService:getResultSearch() startMethod ");
        log.info("AccountService:getResultSearch() accountSearchDto " + accountSearchDto.toString());
        getErrorIfNull(pageable);
        List<UUID> accountBlocked = List.of();
        if(accountSearchDto.getShowFriends()!=null&&!accountSearchDto.getShowFriends()){
            accountBlocked = friendService.getAllInRelationShips().stream().map(account -> UUID.fromString(account)).collect(Collectors.toList());
        }
        accountBlocked = accountBlocked.size() == 0 ? null : accountBlocked;
        log.info("AccountService:getResultSearch() accountBlocked " + accountBlocked);
        Specification spec = like(Account_.COUNTRY, accountSearchDto.getCountry())
                .and(notEqual(Account_.ID, AuthUtil.getUserId()))
                .and(notEqualIn(Account_.ID, accountBlocked))
                .and(like(Account_.FIRST_NAME, accountSearchDto.getFirstName()))
                .and(like(Account_.LAST_NAME, accountSearchDto.getLastName()))
                .and(like(Account_.CITY, accountSearchDto.getCity()))
                .and(equal(Account_.EMAIL, accountSearchDto.getEmail()))
                .and(equal(Account_.IS_DELETED, false))
                .and(between(Account_.BIRTH_DATE, accountSearchDto.getAgeFrom(), accountSearchDto.getAgeTo()));
        if (Objects.nonNull(accountSearchDto.getIds())) {
            if (accountSearchDto.getIds().size() > 0) {
                spec = spec.and(in(Account_.ID, accountSearchDto.getIds()));
                log.info("AccountService:getResultSearch() spec Ids " + spec.toString());
            }
        }
        if (accountSearchDto.getAuthor() != null) {
            spec = spec.and(like(Account_.FIRST_NAME, accountSearchDto.getAuthor()));
            log.info("AccountService:getResultSearch() spec athorb " + spec.toString());
        }
        Page<Account> accounts = accountRepository.findAll(spec, pageable);
        Page<AccountDto> accountDtos = accounts.map(mapperAccount::toDto);
        accountDtos = setStatus(accountDtos);
        log.info("AccountService:getResultSearch() return list-> " + accountDtos.stream().map(a->a.getId()).toList());
        return accountDtos;
    }

    @Logging
    private Page<AccountDto> setStatus(Page<AccountDto> accounts) {
        log.info("AccountService:setStatus() startMethod");
        Map<String, String> statusFrend = friendService.getFriendsStatus(accounts.stream().map(a -> a.getId()).collect(Collectors.toList()));
        for (String key : statusFrend.keySet()) {
            for (AccountDto account : accounts) {
                if (UUID.fromString(key).equals(account.getId())) {
                    account.setStatusCode(StatusCode.valueOf(statusFrend.get(key)));
                }
            }
        }
        return accounts;
    }

    @Logging
    public Page<AccountDto> getAll(AccountSearchDto accountSearchDto, Pageable pageable) {
        log.info("AccountService:getAll() startMethod");
        getErrorIfNull(accountSearchDto);
        getErrorIfNull(pageable);
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

    @Logging
    public AccountDto changePassword(PasswordChangeDto passwordChangeDto) {
        if (!passwordChangeDto.getNewPassword1().equals(passwordChangeDto.getNewPassword2())) {
            new AccountException("введенные пароли не совпадают");
        }
        AccountDto accountDto = new AccountDto();
        accountDto.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword1()));
        accountDto.setId(AuthUtil.getUserId());
        update(accountDto);
        return update(accountDto);
    }

    @Logging
    public AccountDto changeEmail(ChangeEmailDto changeEmailDto) {
        AccountDto accountDto = new AccountDto();
        accountDto.setEmail(changeEmailDto.getEmail().getEmail());
        accountDto.setId(AuthUtil.getUserId());
        return update(accountDto);
    }

    @Scheduled(cron = "${cron.wishHappyBirthday}")
    public void sendNotificationsHappyBirthday() {
        String messageHappyBirthday = "Наша соцсеть поздравляет вас с Днем Рождения!";
        String messageForFriends = "У вашего друга ?name сегодня День Роджения. Не забудьте поздравить его!";
        List<Object[]> objects = accountRepository.findAllByBirthDate(LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue());

        objects.stream().map(object -> new NotificationDTO((UUID) object[0], (UUID) object[0], messageHappyBirthday
                        , Type.USER_BIRTHDAY, ZonedDateTime.now()))
                .toList().forEach(kafkaService::sendNotification);
        objects.stream().map(object -> new NotificationDTO((UUID) object[0], (UUID) object[0]
                        , messageForFriends.replace("?name"
                        , getId((UUID) object[0]).getFirstName() + " " + getId((UUID) object[0]).getLastName())
                        , Type.FRIEND_BIRTHDAY, ZonedDateTime.now()))
                .toList().forEach(kafkaService::sendNotification);
    }

    @Logging
    private void getErrorIfNull(Object object) {
        if ((object == null)) {
            throw new AccountException("Нет данных пользователя");
        }
    }

    public Timestamp getLastOnlineTime() {
        log.info("AccountService:getLastOnlineTime() - startMethod");
        Timestamp lastTimestamp = accountRepository.findTopDate().orElse(new Timestamp(System.currentTimeMillis()));
        if(lastTimestamp == null){
            lastTimestamp = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());
        }
        log.info("AccountService:getLastOnlineTime() - получен Timestamp LastOnlineTime: {}", lastTimestamp);
        return lastTimestamp;
    }

}
