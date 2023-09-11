package ru.skillbox.diplom.group40.social.network.api.resource.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;


@RequestMapping("api/v1/auth/")
@Controller
public interface AuthController {

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto loginDto);

    @PostMapping ("/logout")
    public ResponseEntity<String> logout();

    @GetMapping("/test")
    public ResponseEntity<String> test ();

}
