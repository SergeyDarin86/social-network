package ru.skillbox.diplom.group40.social.network.impl.resource.auth;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import ru.skillbox.diplom.group40.social.network.api.dto.account.ChangeEmailDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.PasswordChangeDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.api.resource.auth.AuthController;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
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
    private final AccountService accountService;
    private final MapperAccount mapperAccount;

    @Timed(value = "timed.login")
    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        System.out.println("login");
        AuthenticateResponseDto authenticateResponseDto = authService.login(authenticateDto);
        return ResponseEntity.ok(authenticateResponseDto);
    }
    @Timed(value = "timed.register")
    @Override
    public ResponseEntity<String> register(RegistrationDto registrationDto) {
        captchaService.checkCaptcha(registrationDto.getCaptchaCode(), registrationDto.getCaptchaSecret());
        authService.register(registrationDto);
        return ResponseEntity.ok("registered");
    }

    @Override
    public ResponseEntity<AuthenticateResponseDto> refresh(RefreshDto refreshDto) {
        AuthenticateResponseDto authenticateResponseDto = authService.refresh(refreshDto);
        return ResponseEntity.ok(authenticateResponseDto);
    }

    @Override
    public ResponseEntity<String> sendRecoveryEmail(PasswordRecoveryDto dto) {
        recoveryService.recover(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> changePassword(String recoveryTokenId, NewPasswordDto passwordDto) {
        recoveryService.setNewPassword(recoveryTokenId, passwordDto);
        return ResponseEntity.ok().build();
    }
    @Timed(value = "timed.logout")
    @Override
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("logged out");
    }

    @Override
    public ResponseEntity<CaptchaDto> getCaptcha() {

        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @Override
    public ResponseEntity<String> getUsers() {
        String userList = authService.getActiveUserList();
        return  ResponseEntity.ok(userList);
    }

    @Override
    public ResponseEntity<String> revokeUserTokens(String email) {
        authService.revokeUserTokens(email);
        return ResponseEntity.ok("done");
    }

    @Override
    public ResponseEntity<String> revokeAllTokens() {
        authService.revokeAllTokens();
        return ResponseEntity.ok("done");
    }


    @Override
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello");
    }

    @Override
    public ResponseEntity<AccountDto> changePasswordLink(PasswordChangeDto newAggregateEmailDto) {
        return ResponseEntity.ok(accountService.changePassword(newAggregateEmailDto));
    }
    @Override
    public ResponseEntity<AccountDto> changeEmailLink(ChangeEmailDto newAggregateEmailDto) {
        return ResponseEntity.ok(accountService.changeEmail(newAggregateEmailDto));
    }
}
