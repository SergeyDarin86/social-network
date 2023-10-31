package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import lombok.extern.log4j.Log4j;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;

import java.time.LocalDateTime;


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
    @AfterMapping
    protected void setDefaultIsDeletedIfNull(RegistrationDto dto, @MappingTarget AccountDto accountDto) {
        if (accountDto.getIsDeleted() == null) {
            accountDto.setIsDeleted(false);
        }
    }

    public abstract AccountDto AccountDtoFromAccountOnLineDto(AccountOnlineDto record);

    public abstract AccountOnlineDto getAccountOnlineDtoFromAccount(Account account);

    public abstract AccountDto AccountDtoFromAgregatEmailDto(AuthenticateDto authenticateDto);

    public static LocalDateTime getTime(){
        return LocalDateTime.now();
    }
}
