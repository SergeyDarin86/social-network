package ru.skillbox.diplom.group40.social.network.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.AuthService;
import ru.skillbox.diplom.group40.social.network.impl.unit.FactoryTest;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.technikalUser.TechnicalUserConfig;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
        /*@RequiredArgsConstructor*/
class controllerTest {

    /*@LocalServerPort
    private Integer port;*/
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    FactoryTest factoryTest;
    @Autowired
    AuthService authService;
    @Autowired
    JwtEncoder jwtEncoder;
    @Autowired
    TechnicalUserConfig technicalUser;

    @Autowired
    JwtEncoder accessTokenEncoder;
    @Autowired
    MapperAccount mapperAccount;

    private List<AccountDto> accountDtos;


    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withInitScript("ru/skillbox/diplom/group40/social/network/impl/schema.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    /* @AfterAll
    static void afterAll() {
        postgres.stop();
    }*/


/*    @Test
    void  postUnit() throws Exception {
        var result = mockMvc.perform(get("http://localhost:8080/api/v1/auth/test"))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();// не смог десерилизовать LIST
        Assertions.assertEquals("hello", result);
        accountService.create(factoryTest.getAccountDto());
        List<Account> resultAccount = accountRepository.findAll();
        Assertions.assertEquals(3, resultAccount.size());
        Assertions.assertEquals("test@email.ru", resultAccount.get(2).getEmail());
    }*/

    private void initData() {
        technicalUser.executeByTechnicalUser(() -> 2);
        accountDtos = factoryTest.getListTestAccountDto();
        accountDtos = accountDtos.stream().map(account -> accountService.create(account)).collect(Collectors.toList());
        accountDtos = accountService.getResultSearch(factoryTest.getMapSearchDto().get("isDeletedFalse"), factoryTest.getPagable()).getContent();
    }

    @Test
    @Transactional
    void testAccountCreate() {
        initData();
        int res = accountRepository.findAll().size();
        Assertions.assertEquals(accountDtos.size(), res);
    }

    @Test
    @Transactional
    void testAccountUpdate() {
        initData();
        accountDtos = accountService.getResultSearch(factoryTest.getMapSearchDto().get("isDeletedFalse"), factoryTest.getPagable()).getContent();
        for (AccountDto accountDto : accountDtos) {
            accountDto.setFirstName("Update" + accountDto.getFirstName());
        }
        accountDtos.stream().map(account -> accountService.update(account)).collect(Collectors.toList());
        Assertions.assertTrue(accountDtos.stream().allMatch(accountDto -> accountDto.getFirstName().contains("Update")));
    }

    @Test
    @Transactional
    void testSoftDelete() {
        initData();
        AccountSearchDto searchDto = factoryTest.getMapSearchDto().get("isDeletedFalse");
        List<AccountDto> accountDtosRes = accountService.getResultSearch(searchDto, factoryTest.getPagable()).getContent();
        accountDtos.stream().map(account -> accountService.deleteById(account.getId())).collect(Collectors.toList());
        List<Account> accounts = accountRepository.findAll();
        Assertions.assertEquals(accountDtosRes.size(), accounts.size());
        Assertions.assertTrue(accounts.stream().allMatch(account -> account.getIsDeleted()));
    }

    @Test
    @Transactional
    void testSearchAge() {
        initData();
        AccountSearchDto searchDto = factoryTest.getMapSearchDto().get("age25_35");
        List<AccountDto> accountDtosRes = accountService.getResultSearch(searchDto, factoryTest.getPagable()).getContent();
        Assertions.assertEquals(accountDtosRes.size(), 1);
    }

    @Test
    @Transactional
    void testCity() {
        initData();
        AccountSearchDto searchDto = factoryTest.getMapSearchDto().get("City");
        List<AccountDto> accountDtosRes = accountService.getResultSearch(searchDto, factoryTest.getPagable()).getContent();
        Assertions.assertEquals(accountDtosRes.size(), 1);
        Assertions.assertTrue(accountDtosRes.stream().allMatch(accountDto -> accountDto.getCity().equals(searchDto.getCity())));
    }

    @Test
    @Transactional
    void testFindAll() {
        initData();
        AccountSearchDto searchDtoAll = factoryTest.getMapSearchDto().get("all");
        AccountSearchDto searchDtoNothing = factoryTest.getMapSearchDto().get("nothing");
        List<AccountDto> accountDtosResAll = accountService.getAll(searchDtoAll, factoryTest.getPagable()).getContent();
        List<AccountDto> accountDtosResNothing = accountService.getAll(searchDtoNothing, factoryTest.getPagable()).getContent();
        Assertions.assertTrue(accountDtosResAll.size() >= 3);
        Assertions.assertTrue(accountDtosResNothing.size() == 0);
    }

    @Test
    @Transactional
    void testGetById() {
        initData();
        UUID id = accountDtos.get(2).getId();
        UUID idRes = accountService.getId(id).getId();
        Assertions.assertEquals(idRes, id);
    }

    @Test
    @Transactional
    void testGetByEmail() {
        initData();
        String email = accountDtos.get(2).getEmail();
        String emailRes = accountService.getByEmail(email).getEmail();
        Assertions.assertEquals(emailRes, email);
    }

    /*    @Test
        @Transactional
        void testGetMe() {
            initData();
            AccountDto accountDtoVirtual = new AccountDto();
            accountDtoVirtual.setId(AuthUtil.getUserId());
            Account accountVirtual = accountRepository.save(mapperAccount.toEntity(accountDtoVirtual));
            AccountDto accountRes = accountService.getMe();
            Assertions.assertEquals(accountRes.getId(), accountVirtual.getId());
        }*/
    @Test
    @Transactional
    void testLogin() throws Exception {
        initData();
        AuthenticateResponseDto authenticateResponseDto = authService.login(factoryTest.getAuthenticateDto());
        String token = authenticateResponseDto.getAccessToken();
        /*RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/v1/account/test";
        ResponseEntity<String> response
                = restTemplate.getForEntity(fooResourceUrl, String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);*/
        TestRestTemplate restTemplate = new TestRestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/api/v1/account/test";
        //ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl,  HttpMethod.GET, null, String.class);
        ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, new HttpEntity<>(createHeaders(token)), String.class);
        /*var result = mockMvc.perform(get("http://localhost:8080/api/v1/auth"))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();*/



        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    HttpHeaders createHeaders(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        return httpHeaders;
    }
}