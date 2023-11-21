package ru.skillbox.diplom.group40.social.network.impl.mapper.account;


import lombok.extern.log4j.Log4j;
import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountOnlineDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.AuthenticateDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


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

    @Mapping(target="authorId", source="account.id")
    @Mapping(target="content", source="account.id")
    @Mapping(target = "notificationType", expression = "java(getType())")
    public abstract NotificationDTO toNotificationDTO(Account account);

    public static  Type getType() {
        return Type.FRIEND_BIRTHDAY;
        };


}
