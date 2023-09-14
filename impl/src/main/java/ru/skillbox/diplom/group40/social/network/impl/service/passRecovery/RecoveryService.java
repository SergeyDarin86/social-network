package ru.skillbox.diplom.group40.social.network.impl.service.passRecovery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.NewPasswordDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.passwordRecovery.RecoveryToken;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.recoveryTokenRepository.RecoveryTokenRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.utils.mail.MailUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecoveryService {
    private final AccountService accountService;
    private final RecoveryTokenRepository tokenRepository;
    private final MailUtil mailUtil;
    private final MapperAccount accountMapper;
    private final String host = "localhost:8099";

    public Boolean recover(String email) {
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            return false;
        }
        RecoveryToken token = new RecoveryToken(account, LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
        tokenRepository.save(token);
        mailUtil.send(email, getMessage(token.getId()));
        return true;
    }

    private String getMessage(UUID tokenId) {
        return "для восстановления пароля перейдите по ссылке:\n" +
                "http://" + host + "/change-password/" + tokenId.toString();
    }

    public boolean setNewPassword(String linkId, NewPasswordDto passwordDto) {
        Optional<RecoveryToken> optionalToken = tokenRepository.findById(UUID.fromString(linkId));
        if (optionalToken.isEmpty()) {
            return false;
        }
        RecoveryToken token = optionalToken.get();
        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        Account account = token.getAccount();
        account.setPassword(passwordDto.getPassword());
        accountService.save(account);
        tokenRepository.delete(token);
        return true;
    }
}
