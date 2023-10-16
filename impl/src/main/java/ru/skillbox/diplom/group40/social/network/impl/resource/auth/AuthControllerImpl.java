package ru.skillbox.diplom.group40.social.network.impl.resource.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.api.resource.auth.AuthController;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.AuthService;
import ru.skillbox.diplom.group40.social.network.impl.service.passRecovery.RecoveryService;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.CaptchaService;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final CaptchaService captchaService;
    private final RecoveryService recoveryService;
    private final AuthService authService;


    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        System.out.println("login");
        AuthenticateResponseDto authenticateResponseDto = authService.login(authenticateDto);
        return ResponseEntity.ok(authenticateResponseDto);
    }

    @Override
    public ResponseEntity<String> register(RegistrationDto registrationDto) {
        captchaService.checkCaptcha(registrationDto.getCaptchaCode(), registrationDto.getCaptchaSecret());
        authService.register(registrationDto);
        return ResponseEntity.ok("registered");
    }

    @Override
    public ResponseEntity<String> sendRecoveryEmail(PasswordRecoveryDto dto) {
        recoveryService.recover(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> changePassword(String linkId, NewPasswordDto passwordDto) {
        recoveryService.setNewPassword(linkId, passwordDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("logged out");
    }

    @Override
    public ResponseEntity<CaptchaDto> getCaptcha() {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }


    @Override
    public ResponseEntity<String> test(String param1, String param2, RegistrationDto registrationDto) {
        System.out.println(registrationDto);
        System.out.println(param1);
        System.out.println(param2);
        return ResponseEntity.ok("hello");
    }
}
