package ru.skillbox.diplom.group40.social.network.impl.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.security.jwt.TokenGenerator;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;

import javax.security.auth.login.AccountException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountService accountServices;
    private final MapperAccount mapperAccount;
    private final TokenGenerator tokenGenerator;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        JwtDto jwtDto = accountServices.getJwtDto(authenticateDto);
        AuthenticateResponseDto responseDto = new AuthenticateResponseDto();
        responseDto.setToken(tokenGenerator.createToken(jwtDto));
        return responseDto;
    }

    public void register(RegistrationDto registrationDto) {
        AccountDto accountDto = mapperAccount.accountDtoFromRegistrationDto(registrationDto);
        try {
            accountServices.create(accountDto);
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
    }



    public void testRegister(RegistrationDto loginDto) {    //временный метод для тестов
        //можно написать свой код

    }


}
