package ru.skillbox.diplom.group40.social.network.impl.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.user.User;

import java.time.LocalDateTime;
import java.util.*;
@Component
public class FactoryTest {
    @Autowired
    PasswordEncoder passwordEncoder;


    private UUID id = UUID.fromString("fb4a3a33-1b8e-4675-a5dc-a3897764b6a5");
    private String email = "test@email.ru";
    private AccountDto accountDto;
    private Account account;

    private List<AccountDto> listAccounDto = new ArrayList<>();
    private static List<Account> listTestAccount;
    private static HashMap<String, AccountSearchDto> mapSearchDto;

    public AccountDto getAccountDto() {
        accountDto = new AccountDto();
        accountDto.setEmail(email);
        accountDto.setId(id);
        return accountDto;
    }

    public AccountDto getUpdatedAccountDto() {
        accountDto = new AccountDto();
        accountDto.setEmail(email+".updated");
        accountDto.setId(id);
        return accountDto;
    }

    public Account getAccount() {
        account = new Account();
        account.setEmail(email);
        account.setId(id);
        return account;
    }
    public Account getUpdatedAccount() {
        account = new Account();
        account.setEmail(email+".updated");
        account.setId(id);
        return account;
    }


    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public List<Account> getListTestAccount(){
        listTestAccount = new ArrayList<>();
        LocalDateTime birthdate = LocalDateTime.of(1983,12,1,0,0);
        LocalDateTime birthdate2 = LocalDateTime.of(1993,12,1,0,0);
        LocalDateTime birthdate3 = LocalDateTime.of(2003,12,1,0,0);
        Account account1 = new Account("","","","","Moscou","Russia", StatusCode.FRIEND,birthdate,"",null,true,false,"",null);
        account1.setFirstName("Taras");
        account1.setLastName("Ivanov");
        account1.setIsDeleted(false);
        Account account2 = new Account("","","","","Moscou","Russia", StatusCode.FRIEND,birthdate2,"",null,true,false,"",null);account1.setFirstName("Taras");
        account2.setFirstName("Taras");
        account2.setLastName("Petrov");
        account2.setIsDeleted(false);
        Account account3 = new Account("","","","","Moscou","Russia", StatusCode.FRIEND,birthdate3,"",null,true,false,"",null);
        account3.setFirstName("Taras");
        account3.setLastName("Semenov");
        account3.setIsDeleted(false);



        Account account11 = new Account("","","","","Tiksi","Russia", StatusCode.FRIEND,birthdate,"",null,true,false,"",null);
        account11.setFirstName("Vasia");
        account11.setLastName("Ivanov");
        account11.setIsDeleted(false);
        Account account22 = new Account("","","","","Tiksi","Russia", StatusCode.FRIEND,birthdate2,"",null,true,false,"",null);
        account22.setFirstName("Vasia");
        account22.setLastName("Petrov");
        account22.setIsDeleted(false);
        Account account33 = new Account("","","","","Tiksi","Russia", StatusCode.FRIEND,birthdate3,"",null,true,false,"",null);
        account33.setFirstName("Vasia");
        account33.setLastName("Semenov");
        account33.setIsDeleted(false);
        listTestAccount.add(account1);
        listTestAccount.add(account2);
        listTestAccount.add(account3);
        listTestAccount.add(account11);
        listTestAccount.add(account22);
        listTestAccount.add(account33);
        return listTestAccount;
    }
    public List<AccountDto> getListTestAccountDto(){
        List<AccountDto> dtos = new ArrayList<>();
        AccountDto accountDto1 = createAccountDto("Taras", "Ivanov", "Moscou","Russia", LocalDateTime.of(1983,12,1,0,0), false);
        accountDto1.setEmail("account1@email.ru");
        accountDto1.setPassword(passwordEncoder.encode("11111111"));
        AccountDto accountDto2 = createAccountDto("Taras", "Semenov", "Spb","Russia", LocalDateTime.of(1993,12,1,0,0), false);
        accountDto2.setEmail("account2@email.ru");
        AccountDto accountDto3 = createAccountDto("Taras", "Petrov", "Murmansk","Russia", LocalDateTime.of(2003,12,1,0,0), false);
        accountDto3.setEmail("account3@email.ru");
        dtos.add(accountDto1);
        dtos.add(accountDto2);
        dtos.add(accountDto3);
        return dtos;
    }

    private AccountDto createAccountDto(String name, String lastName, String city, String country, LocalDateTime of, boolean b) {
        AccountDto accountDto1 = new AccountDto();
        accountDto1.setId(UUID.randomUUID());
        accountDto1.setFirstName(name);
        accountDto1.setLastName(lastName);
        accountDto1.setCity(city);
        accountDto1.setCountry(country);
        accountDto1.setBirthDate(of);
        accountDto1.setIsDeleted(b);
        accountDto1.setLastOnlineTime(LocalDateTime.now());
        return accountDto1;
    }

    public HashMap<String, AccountSearchDto> getMapSearchDto(){
        mapSearchDto = new HashMap<>();
        AccountSearchDto accountSearchTaras = new AccountSearchDto();
        accountSearchTaras.setFirstName("Taras");
        mapSearchDto.put("searchTaras", accountSearchTaras);
        AccountSearchDto allAge = new AccountSearchDto();
        allAge.setAgeFrom(0);
        allAge.setAgeTo(300);
        mapSearchDto.put("allAge", allAge);
        AccountSearchDto age25_35 = new AccountSearchDto();
        age25_35.setAgeFrom(25);
        age25_35.setAgeTo(35);
        mapSearchDto.put("age25_35", age25_35);
        AccountSearchDto isDeletedFalse = new AccountSearchDto();
        isDeletedFalse.setIsDeleted(false);
        mapSearchDto.put("isDeletedFalse", isDeletedFalse);
        AccountSearchDto city = new AccountSearchDto();
        city.setCity("Murmansk");
        mapSearchDto.put("City", city);

        AccountSearchDto all = new AccountSearchDto();
        all.setCity("Spb");
        all.setLastName("Ivanov");
        all.setCountry("Russia");
        mapSearchDto.put("all", all);

        AccountSearchDto nothing = new AccountSearchDto();
        nothing.setCity("Genuy");
        nothing.setLastName("Smit");
        nothing.setCountry("Usa");
        mapSearchDto.put("nothing", nothing);
        return mapSearchDto;

    }
    public Pageable getPagable(){
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 10;
            }

            @Override
            public int getPageSize() {
                return 10;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return Sort.unsorted();
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
        return pageable;
    }

    public AuthenticateDto getAuthenticateDto(){
        AuthenticateDto authenticateDto = new AuthenticateDto();
        authenticateDto.setPassword("11111111");
        authenticateDto.setEmail("account1@email.ru");
        return authenticateDto;
    }
}
