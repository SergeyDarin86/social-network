package ru.skillbox.diplom.group40.social.network.impl.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.domain.user.User;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.user.UserRepository;
import ru.skillbox.diplom.group40.social.network.impl.security.jwt.TokenGenerator;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;

import javax.security.auth.login.AccountException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountService accountServices;
    private final MapperAccount mapperAccount;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    public AuthenticateResponseDto login(AuthenticateDto authenticateDto){
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticateDto.getEmail());
        if (optionalUser.isEmpty()){
            throw new AuthException("no such user");
        }
        User user = optionalUser.get();
        if(!user.getPassword().equals(authenticateDto.getPassword())){
            throw new AuthException("wrong password");
        }

        JwtDto jwtDto = new JwtDto();
        jwtDto.setId(user.getId());
        jwtDto.setEmail(user.getEmail());
        jwtDto.setRoles(listOfRolesFromSetOfRoles(user.getRoles()));
        try {
            updateLastOnlineTime(user.getId());
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
        AuthenticateResponseDto responseDto = new AuthenticateResponseDto();
        responseDto.setAccessToken(tokenGenerator.createToken(jwtDto));
        responseDto.setRefreshToken("Здесь будет рефреш токен");
        return responseDto;
    }

    public void register(RegistrationDto registrationDto) {
        checkIfUserWithSuchEmailExist(registrationDto.getEmail());
        AccountDto accountDto = mapperAccount.accountDtoFromRegistrationDto(registrationDto);
        try {
            accountServices.create(accountDto);
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLastOnlineTime(UUID id) throws AccountException {
        AccountDto accountDto = accountServices.getId(id);
        accountDto.setId(id);
        accountDto.setLastOnlineTime(LocalDateTime.now());
        accountServices.update(accountDto);
    }

    private List<String> listOfRolesFromSetOfRoles(Set<Role> roles) {
        ArrayList<String> roleNames = new ArrayList<>();
        for(Role role : roles){
            roleNames.add(role.getRole());
        }
        return roleNames;
    }
    private void checkIfUserWithSuchEmailExist(String email){
       if(userRepository.findFirstByEmail(email).isPresent()){
           throw new AuthException("email taken");
       }
    }
}
