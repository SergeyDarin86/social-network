package ru.skillbox.diplom.group40.social.network.impl.service.passRecovery;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.NewPasswordDto;
import ru.skillbox.diplom.group40.social.network.domain.passwordRecovery.RecoveryToken;
import ru.skillbox.diplom.group40.social.network.domain.user.User;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;
import ru.skillbox.diplom.group40.social.network.impl.repository.recoveryToken.RecoveryTokenRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.user.UserRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.mail.MailUtil;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RecoveryService {
    private final UserRepository userRepository;
    private final RecoveryTokenRepository tokenRepository;
    private final MailUtil mailUtil;
    @Value("${spring.mail.linkHost}")
    private String host;

    public void recover(String email) {
        Optional<User> optionalUser = userRepository.findFirstByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new AuthException("no such user");
        }
        RecoveryToken token = new RecoveryToken(
                optionalUser.get().getId(),
                ZonedDateTime.now().plus(10, ChronoUnit.MINUTES));
        tokenRepository.save(token);
        mailUtil.send(email, getMessage(token.getId()));
    }

    private String getMessage(UUID tokenId) {
        return "для восстановления пароля перейдите по ссылке:\n" +
                "http://" + host + "/change-password/" + tokenId.toString();
    }

    public void setNewPassword(String linkId, NewPasswordDto passwordDto) {
        Optional<RecoveryToken> optionalToken = tokenRepository.findById(UUID.fromString(linkId));
        if (optionalToken.isEmpty()) {
            throw new AuthException("wrong link");
        }
        RecoveryToken token = optionalToken.get();
        if (token.getExpirationTime().isBefore(ZonedDateTime.now())) {
            throw new AuthException("recovery token expired");
        }
        Optional<User> optionalUser = userRepository.findFirstById(token.getUserId());
        if(optionalUser.isEmpty()){
            throw new AuthException("no such user");
        }
        User user = optionalUser.get();
        user.setPassword(passwordDto.getPassword());
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}
