package ru.skillbox.diplom.group40.social.network.impl.utils.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.JwtDto;
import ru.skillbox.diplom.group40.social.network.domain.base.audit.UserJsonType;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UserJsonType> {

    @Override
    public Optional<UserJsonType> getCurrentAuditor() {
        JwtDto jwtDto = AuthUtil.getJwtDto();
        if (jwtDto==null){
            return Optional.of(unauthorizedJson());
        }
        UserJsonType json = new UserJsonType(jwtDto.getId().toString(), jwtDto.getEmail());
        return Optional.of(json);
    }

    private UserJsonType unauthorizedJson(){
        return new UserJsonType();
    }
}