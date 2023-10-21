package ru.skillbox.diplom.group40.social.network.impl.resource.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SocketNotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.resource.auth.AuthController;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.AuthService;
import ru.skillbox.diplom.group40.social.network.impl.service.passRecovery.RecoveryService;
import ru.skillbox.diplom.group40.social.network.impl.service.auth.CaptchaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final CaptchaService captchaService;
    private final RecoveryService recoveryService;
    private final AuthService authService;
    private final WebSocketHandler webSocketHandler;



    @Override
    public ResponseEntity<AuthenticateResponseDto> login(AuthenticateDto authenticateDto) {
        System.out.println("login");
        AuthenticateResponseDto authenticateResponseDto = authService.login(authenticateDto);
        if(authenticateResponseDto.getAccessToken()==null){
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok(authenticateResponseDto);
    }

    @Override
    public ResponseEntity<String> register(RegistrationDto registrationDto) {
        if(!captchaService.captchaIsCorrect(registrationDto.getCaptchaCode(),registrationDto.getCaptchaSecret())){
            return ResponseEntity.badRequest().body("wrong captcha code");
        }
        if(!authService.register(registrationDto)){
            return ResponseEntity.badRequest().body("email taken");
        }
        System.out.println("registered");
        return ResponseEntity.ok("registered");
    }

    @Override
    public ResponseEntity<String> sendRecoveryEmail(PasswordRecoveryDto dto) {
        if(!recoveryService.recover(dto.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> changePassword(String linkId, NewPasswordDto passwordDto) {
        if(recoveryService.setNewPassword(linkId, passwordDto)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<String> logout() {
        closeWebsocket();
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

    /** Вынести в сервис для обработки логаута*/
    public boolean closeWebsocket() {
        UUID id = AuthUtil.getUserId();

        List<WebSocketSession> sendingList = webSocketHandler.getSessionMap()
                .getOrDefault(id, new ArrayList<>());

        if (sendingList.isEmpty()) {
            return false;
        }

        try {
            webSocketHandler.afterConnectionClosed(sendingList.get(0), CloseStatus.NORMAL);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /***/

}
