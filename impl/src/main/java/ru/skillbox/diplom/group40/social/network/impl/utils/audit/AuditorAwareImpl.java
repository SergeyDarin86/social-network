package ru.skillbox.diplom.group40.social.network.impl.utils.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AccessJwtDto;
import ru.skillbox.diplom.group40.social.network.domain.base.audit.UserJsonType;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UserJsonType> {

    @Override
    public Optional<UserJsonType> getCurrentAuditor() {
        AccessJwtDto accessJwtDto = AuthUtil.getJwtDto();
        if (accessJwtDto ==null){
            return Optional.of(unauthorizedJson());
        }
        UserJsonType json = new UserJsonType(accessJwtDto.getUserId(), accessJwtDto.getEmail());
        return Optional.of(json);
    }

    private UserJsonType unauthorizedJson(){
        return new UserJsonType();
    }
}