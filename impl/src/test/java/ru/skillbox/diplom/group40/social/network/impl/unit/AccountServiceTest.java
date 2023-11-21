package ru.skillbox.diplom.group40.social.network.impl.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccountImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    MapperAccountImpl mapperAccount;

    @Mock
    AuthUtil authUtil;


    @InjectMocks
    AccountService accountService;
    FactoryTest factoryTest;

    AccountDto accountDto, accountUpdateDto;
    Account account, accountUpdate;
    @BeforeEach
    void init(){
        factoryTest = new FactoryTest();
        accountDto = factoryTest.getAccountDto();
        accountUpdateDto = factoryTest.getUpdatedAccountDto();
        account = factoryTest.getAccount();
        accountUpdate = factoryTest.getUpdatedAccount();
    }

    @Test
    void tesGetByEmail() {
        Mockito.when(mapperAccount.toDto(account))
                .thenReturn(accountDto);
        Mockito.when(accountRepository.findFirstByEmail(account.getEmail())).thenReturn(Optional.of(account));
        AccountDto result = accountService.getByEmail(factoryTest.getEmail());
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof AccountDto);
        Assertions.assertEquals(result, accountDto);
    }

    @Test
    void testGetId() {
        Mockito.when(mapperAccount.toDto(account))
                .thenReturn(accountDto);
        Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        AccountDto result = accountService.getId(factoryTest.getId());
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof AccountDto);
        Assertions.assertEquals(result, accountDto);
    }

    @Test
    void testUpdate() {
        Mockito.when(accountRepository.findById(accountDto.getId()))
                .thenReturn(Optional.of(account));
        Mockito.when(mapperAccount.rewriteEntity(accountRepository.findById(account.getId()).get(), accountUpdateDto))
                .thenReturn(accountUpdate);
        Mockito.when(mapperAccount.toDto(accountUpdate)).thenReturn(accountUpdateDto);
        AccountDto result = accountService.update(accountUpdateDto);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof AccountDto);
        Assertions.assertEquals(result, accountUpdateDto);
    }
}