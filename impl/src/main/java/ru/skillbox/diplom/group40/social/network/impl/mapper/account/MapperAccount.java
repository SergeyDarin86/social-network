package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import lombok.extern.log4j.Log4j;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;


@Log4j
@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class MapperAccount {

    public abstract AccountDto toDto(Account account);
    @Mapping(target = "isBlocked", source = "accountDto.isBlocked", defaultValue = "false")
    public abstract Account toEntity(AccountDto accountDto);
    public abstract Account rewriteEntity(@MappingTarget Account account, AccountDto accountDto);
    @Mapping(target = "password", source = "dto.password1")
    public abstract AccountDto accountDtoFromRegistrationDto(RegistrationDto dto);

    public abstract Account rewriteEntity(@MappingTarget Account account, AccountOnlineDto accountOnlineDto);

    public abstract AccountDto AccountDtoFromAccountOnLineDto(AccountOnlineDto record);

    public abstract AccountOnlineDto getAccountOnlineDtoFromAccount(Account account);
}
