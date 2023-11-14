package ru.skillbox.diplom.group40.social.network.impl.service.account;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.role.RoleService;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private  AccountRepository accountRepository;
    @Mock
    private  MapperAccount mapperAccount;
    private  String BADREUQEST = "bad reqest";
    @Mock
    FriendService friendService;
    @Mock
//    private  NotificationService notificationService;
    private NotificationSettingsService notificationSettingsService;
    @Mock
    private RoleService roleService;

    @Mock
    private  KafkaService kafkaService;

    private   AccountService accountService;
    @Autowired
    private  JwtEncoder accessTokenEncoder;


    @BeforeEach
    void setup() throws Exception{
        createContext();
//        accountService = new AccountService(mapperAccount, accountRepository, friendService, notificationService, roleService, accessTokenEncoder, kafkaService);
        accountService = new AccountService(mapperAccount, accountRepository, friendService, notificationSettingsService, roleService, accessTokenEncoder, kafkaService);
    }

    @Test
    void create() {
        when(accountRepository.save(getAccount())).thenReturn(getAccount());
        when(mapperAccount.toEntity(getAccountDto())).thenReturn(getAccount());
        when(mapperAccount.toDto(getAccount())).thenReturn(getAccountDto());
        AccountDto accountDtoAssert = accountService.create(getAccountDto());
        Assertions.assertNotNull(accountDtoAssert);

    }

    private AccountDto getAccountDto(){
        AccountDto accountDto = new AccountDto();
        accountDto.setIsOnline(true);
        accountDto.setId(UUID.fromString("5a8b1c5f-2619-4bcc-b32c-835dd5aeaae9"));
        return accountDto;
    }
    private Account getAccount(){
        Account account = new Account();
        account.setIsOnline(true);
        account.setId(UUID.fromString("5a8b1c5f-2619-4bcc-b32c-835dd5aeaae9"));
        return account;
    }

    private void createContext() {
        JwtDto jwtDto = new JwtDto();
        jwtDto.setUserId(UUID.randomUUID().toString());
        jwtDto.setEmail("kafka@email");
        jwtDto.setRoles(Collections.singletonList("ROLES_KAFKA"));
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(createToken(jwtDto));
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(jwtAuthenticationToken);
    }

    public Jwt createToken(JwtDto jwtDto) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("myApp")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(jwtDto.getEmail())
                .claim("roles", jwtDto.getRoles())
                .claim("user_id", jwtDto.getUserId())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet));
    }
}