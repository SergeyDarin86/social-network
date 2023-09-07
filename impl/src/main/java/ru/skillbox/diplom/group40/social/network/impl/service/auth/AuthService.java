package ru.skillbox.diplom.group40.social.network.impl.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountServices;

import javax.security.auth.login.AccountException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountServices accountServices;
    private final MapperAccount mapperAccount;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        accountServices.checkAuthDto(authenticateDto);
        AuthenticateResponseDto responseDto = new AuthenticateResponseDto();
        responseDto.setToken("здесь будет токен");
        return responseDto;
    }

    public void register(RegistrationDto registrationDto) {
        AccountDto accountDto = mapperAccount.accountDtoFromRegistrationDto(registrationDto);
        try {
            accountServices.save(accountDto);
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
    }



    public void testRegister(RegistrationDto loginDto) {    //временный метод для тестов
        //можно написать свой код

    }


}
