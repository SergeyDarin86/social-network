package ru.skillbox.diplom.group40.social.network.api.resource.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateResponseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;

@Controller
@RequestMapping("api/v1/auth/")
public interface AuthController {

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto loginDto);

    @PostMapping ("/logout")
    public ResponseEntity<String> logout();

    @PostMapping("/test")
    public ResponseEntity<String> test (
            @RequestParam String param1,
            @RequestParam String param2,
            @RequestBody RegistrationDto registrationDto
    );

}
