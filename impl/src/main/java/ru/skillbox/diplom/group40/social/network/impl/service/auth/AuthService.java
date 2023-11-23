package ru.skillbox.diplom.group40.social.network.impl.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.*;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.domain.user.User;
import ru.skillbox.diplom.group40.social.network.impl.exception.AccountException;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;
import ru.skillbox.diplom.group40.social.network.impl.exception.TokenException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.account.MapperAccount;
import ru.skillbox.diplom.group40.social.network.impl.repository.user.UserRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.security.jwt.TokenGenerator;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;


import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final AccountService accountServices;
    private final MapperAccount mapperAccount;
    private final TokenGenerator tokenGenerator;
    private final UserRepository userRepository;
    @Value("${security.accessTokenLifetime}")
    private Integer accessTokenLifeTime;
    @Value("${security.refreshTokenLifetime}")
    private Integer refreshTokenLifeTime;
    Queue<String> accessTokenExpirationOrder = new ConcurrentLinkedQueue<>();
    Queue<String> refreshTokenExpirationOrder = new ConcurrentLinkedQueue<>();
    Map<String, ZonedDateTime> activeAccessTokens = new ConcurrentHashMap<>();
    Map<String, ZonedDateTime> activeRefreshTokens = new ConcurrentHashMap<>();
    Map<String, List<String>> userEmailToHisAccessTokenIds = new ConcurrentHashMap<>();
    Map<String, List<String>> userEmailToHisRefreshTokenIds = new ConcurrentHashMap<>();


    public AuthenticateResponseDto login(AuthenticateDto authenticateDto) {
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticateDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new AuthException("no such user");
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(authenticateDto.getPassword(),user.getPassword())) {
            throw new AuthException("wrong password");
        }

        AccessJwtDto accessJwtDto = new AccessJwtDto();
        accessJwtDto.setUserId(user.getId().toString());
        accessJwtDto.setEmail(user.getEmail());
        accessJwtDto.setRoles(listOfRolesFromSetOfRoles(user.getRoles()));
        try {
            updateLastOnlineTime(user.getId());
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
        return getAuthenticateResponseDto(accessJwtDto);
    }

    public void register(RegistrationDto registrationDto) {
        checkIfUserWithSuchEmailExist(registrationDto.getEmail());
        registrationDto.setPassword1(passwordEncoder.encode(registrationDto.getPassword1()));
        AccountDto accountDto = mapperAccount.accountDtoFromRegistrationDto(registrationDto);
        try {
            accountServices.create(accountDto);
        } catch (AccountException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticateResponseDto refresh(RefreshDto refreshDto) {
        Jwt refreshJwt = jwtDecoder.decode(refreshDto.getRefreshToken());

        checkIfRefreshTokenIsActive(refreshJwt.getClaim("token_id"));
        activeAccessTokens.remove(refreshJwt.getClaim("access_token_id"));
        activeRefreshTokens.remove(refreshJwt.getClaim("token_id"));
        JwtDto jwtDto = new JwtDto();
        jwtDto.setUserId(refreshJwt.getClaim("user_id"));
        jwtDto.setEmail(refreshJwt.getSubject());
        jwtDto.setRoles(refreshJwt.getClaim("roles"));
        return getAuthenticateResponseDto(jwtDto);
    }

    public void logout() {
        AccessJwtDto accessDto = AuthUtil.getJwtDto();
        String accessTokenUUID = accessDto.getTokenId();
        String refreshTokenUUID = accessDto.getRefreshTokenId();
        activeAccessTokens.remove(accessTokenUUID);
        activeRefreshTokens.remove(refreshTokenUUID);
    }

    public void checkIfRefreshTokenIsActive(String token_id) {
        if (activeRefreshTokens.containsKey(token_id)) {
            log.info("token id found");
            return;
        }
        log.info("token id not found");
        throw new TokenException("there is no such token id");
    }

    public void checkIfAccessTokenIsActive(String token_id) {
        if (activeAccessTokens.containsKey(token_id)) {
            log.info("token id found");
            return;
        }
        log.info("token id not found");
        throw new TokenException("there is no such token id");
    }
    public String getActiveUserList() {
        clearEmailToTokenIdMap(userEmailToHisAccessTokenIds, activeAccessTokens);
        clearEmailToTokenIdMap(userEmailToHisRefreshTokenIds, activeRefreshTokens);
        StringBuilder userList = new StringBuilder();
        for (String email : userEmailToHisRefreshTokenIds.keySet()) {
            Integer accessTokenCount = userEmailToHisAccessTokenIds.getOrDefault(email, new ArrayList<>()).size();
            Integer refreshTokenCount = userEmailToHisRefreshTokenIds.get(email).size();
            userList.append("\n").append(email).append(": ")
                    .append(accessTokenCount).append(" active access and ")
                    .append(refreshTokenCount).append(" active refresh tokens");
        }
        if (userList.isEmpty()) {
            userList.append("no active users");
        }
        return userList.toString();
    }

    public void revokeUserTokens(String userEmail) {
        List<String> accessTokenIds = userEmailToHisAccessTokenIds.getOrDefault(userEmail, new ArrayList<>());
        List<String> refreshTokenIds = userEmailToHisRefreshTokenIds.getOrDefault(userEmail, new ArrayList<>());
        for (String tokenId : accessTokenIds) {
            activeAccessTokens.remove(tokenId);
        }
        for (String tokenId : refreshTokenIds) {
            activeRefreshTokens.remove(tokenId);
        }
    }
    public void revokeAllTokens() {
        activeAccessTokens.clear();
        activeRefreshTokens.clear();
    }

    @Scheduled(fixedDelay = 60_000)
    public void scheduleClearing() {
        log.debug("scheduleClearing started");
        log.debug("collections before clearing:\n");
        log.debug(collectionsToString());
        log.debug("collections after clearing:\n");
        clearQueueAndMap(accessTokenExpirationOrder, activeAccessTokens);
        clearQueueAndMap(refreshTokenExpirationOrder, activeRefreshTokens);
        clearEmailToTokenIdMap(userEmailToHisAccessTokenIds, activeAccessTokens);
        clearEmailToTokenIdMap(userEmailToHisRefreshTokenIds, activeRefreshTokens);
        log.debug(collectionsToString());
    }

    private void clearQueueAndMap(Queue<String> TokenExpirationOrder, Map<String, ZonedDateTime> activeTokensMap) {
        Iterator<String> iterator = TokenExpirationOrder.iterator();
        ZonedDateTime currentTime = ZonedDateTime.now();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (activeTokensMap.containsKey(key)) {
                if (activeTokensMap.get(key).isBefore(currentTime)) {
                    iterator.remove();
                    activeTokensMap.remove(key);
                }
            } else {
                iterator.remove();
            }
        }
    }

    private void clearEmailToTokenIdMap(Map<String, List<String>> userEmailToHisRefreshTokenIds,
                                        Map<String, ZonedDateTime> activeRefreshTokens) {
        List<String> tokensToRemove = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : userEmailToHisRefreshTokenIds.entrySet()) {
            String email = entry.getKey();
            List<String> userTokenIds = entry.getValue();

            for (String tokenId : userTokenIds) {
                if (!activeRefreshTokens.containsKey(tokenId)) {
                    tokensToRemove.add(tokenId);
                }
            }

            for (String tokenId : tokensToRemove) {
                userTokenIds.remove(tokenId);
            }
            tokensToRemove.clear();

            if (userTokenIds.isEmpty()) {
                userEmailToHisRefreshTokenIds.remove(email);
            }
        }
    }

    private String collectionsToString() {
        StringBuilder infoToLog = new StringBuilder();
        infoToLog.append("\n")
                .append("accessTokenExpirationOrder: ").append(accessTokenExpirationOrder.toString()).append("\n")
                .append("refreshTokenExpirationOrder: ").append(refreshTokenExpirationOrder.toString()).append("\n")
                .append("activeAccessTokens: ").append(activeAccessTokens.toString()).append("\n")
                .append("activeRefreshTokens: ").append(activeRefreshTokens.toString()).append("\n")
                .append("userEmailToHisAccessTokenIds: ").append(userEmailToHisAccessTokenIds.toString()).append("\n")
                .append("userEmailToHisRefreshTokenIds: ").append(userEmailToHisRefreshTokenIds.toString()).append("\n")
                .append("\n");
        return infoToLog.toString();
    }

    private AuthenticateResponseDto getAuthenticateResponseDto(JwtDto jwtDto) {
        ZonedDateTime now = ZonedDateTime.now();
        String accessTokenId = generateAndRememberUUID(now, "access", jwtDto.getEmail());
        String refreshTokenId = generateAndRememberUUID(now, "refresh", jwtDto.getEmail());
        String accessToken = tokenGenerator.createAccessToken(jwtDto, accessTokenId, now, refreshTokenId);
        String refreshToken = tokenGenerator.createRefreshToken(jwtDto, refreshTokenId, now, accessTokenId);
        return new AuthenticateResponseDto(accessToken, refreshToken);
    }

    private String generateAndRememberUUID(ZonedDateTime now, String tokenType, String userEmail) {
        String uuid = UUID.randomUUID().toString();
        List<String> ids;
        if (tokenType.equals("access")) {
            accessTokenExpirationOrder.add(uuid);
            activeAccessTokens.put(uuid, now.plus(accessTokenLifeTime, ChronoUnit.MINUTES));
            ids = userEmailToHisAccessTokenIds.getOrDefault(userEmail, new LinkedList<>());
            ids.add(uuid);
            userEmailToHisAccessTokenIds.put(userEmail, ids);
        } else {
            refreshTokenExpirationOrder.add(uuid);
            activeRefreshTokens.put(uuid, now.plus(refreshTokenLifeTime, ChronoUnit.MINUTES));
            ids = userEmailToHisRefreshTokenIds.getOrDefault(userEmail, new LinkedList<>());
            ids.add(uuid);
            userEmailToHisRefreshTokenIds.put(userEmail, ids);
        }
        log.info("saved new token id");
        return uuid;
    }

    private void updateLastOnlineTime(UUID id) throws AccountException {
        AccountDto accountDto = accountServices.getId(id);
        accountDto.setId(id);
        accountDto.setLastOnlineTime(ZonedDateTime.now().toLocalDateTime());
        accountServices.update(accountDto);
    }

    private List<String> listOfRolesFromSetOfRoles(Set<Role> roles) {
        ArrayList<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getRole());
        }
        return roleNames;
    }

    private void checkIfUserWithSuchEmailExist(String email) {
        if (userRepository.findFirstByEmail(email).isPresent()) {
            throw new AuthException("email taken");
        }
    }
}
