package ru.skillbox.diplom.group40.social.network.impl.resource.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.api.resource.auth.AuthController;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.AuthServices;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthServices authServices;


    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        return null;
    }

    @Override
    public ResponseEntity<String> register(RegistrationDto registrationDto) {
        authServices.register(registrationDto);
        return ResponseEntity.ok("registered");
    }

    @Override
    public ResponseEntity<String> test() {      //временный метод для тестов
        authServices.testRegister(null);
        //можно свой код написать
        return ResponseEntity.ok("hello");
    }
}
