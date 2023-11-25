package ru.skillbox.diplom.group40.social.network.api.resource.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.ChangeEmailDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.PasswordChangeDto;

@Controller
@RequestMapping("api/v1/auth/")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegistrationDto loginDto);

    @PostMapping("/refresh")
    ResponseEntity<AuthenticateResponseDto> refresh(@RequestBody RefreshDto refreshDto);

    @PostMapping("/password/recovery/")
    ResponseEntity<String> sendRecoveryEmail(@RequestBody PasswordRecoveryDto recoveryDto);

    @PostMapping("/password/recovery/{linkId}")
    ResponseEntity<String> changePassword(@PathVariable String linkId, @RequestBody NewPasswordDto passwordDto);

    @PostMapping("/logout")
    ResponseEntity<String> logout();

    @GetMapping("/captcha")
    ResponseEntity<CaptchaDto> getCaptcha();

    @GetMapping("/admin/getActiveUsers")
    ResponseEntity<String> getUsers();

    @PostMapping("/admin/revokeUserTokens/{email}")
    ResponseEntity<String> revokeUserTokens(@PathVariable String email);

    @PostMapping("/admin/revokeAllTokens")
    ResponseEntity<String> revokeAllTokens();

    @GetMapping("/test")
    ResponseEntity<String> test();

    @PostMapping("/change-password-link")
    ResponseEntity<AccountDto> changePasswordLink(@RequestBody PasswordChangeDto newAgregatEmailDto);

    @PostMapping("/change-email-link")
    ResponseEntity<AccountDto> changeEmailLink(@RequestBody ChangeEmailDto newAgregatEmailDto);
}
