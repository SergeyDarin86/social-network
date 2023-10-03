package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import lombok.extern.log4j.Log4j;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.Authorities;
import ru.skillbox.diplom.group40.social.network.api.dto.account.RolesDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;

@Log4j
@Component
@Mapper(componentModel = "spring")
public abstract class MapperAccount {

    public abstract AccountDto toDto(Account account);
    public abstract Account toEntity(AccountDto accountDto);

    public AccountDto accountDtoFromRegistrationDto(RegistrationDto dto){
        AccountDto account = new AccountDto();
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword1());
        return account;
    }


}
