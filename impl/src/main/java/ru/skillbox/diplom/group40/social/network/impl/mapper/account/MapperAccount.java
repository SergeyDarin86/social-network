package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skillbox.diplom.group40.social.network.api.dto.account.Authorities;
import ru.skillbox.diplom.group40.social.network.api.dto.account.RolesDto;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;

@Component
@Mapper(componentModel = "spring")
public abstract class MapperAccount {

    public abstract AccountDto toDto(Account account);
    public abstract Account toEntity(AccountDto accountDto);

    public AccountDtoForGet  toDtoForGet(Account account){
        if ( account == null ) {
            return null;
        }

        AccountDtoForGet accountDtoForGet = new AccountDtoForGet();
        accountDtoForGet.setId( account.getId() );
        accountDtoForGet.setDeleted( account.getIsDeleted() );
        accountDtoForGet.setFirstName( account.getFirstName() );
        accountDtoForGet.setEmail( account.getEmail() );
        accountDtoForGet.setPassword( account.getPassword() );
        RolesDto rolesDto = accountDtoForGet.getRoles();
        if(rolesDto==null){
            rolesDto = new RolesDto();
        }
        rolesDto.setId(account.getId());
        rolesDto.setRole("GAGE_ROLE");
        accountDtoForGet.setRoles( rolesDto );

        Authorities authorities = new Authorities();
        if(authorities==null){
            authorities = new Authorities();
        }
        authorities.setId(account.getId());
        authorities.setAuthority("GAG_AUTHORITIES");
        accountDtoForGet.setAuthorities(authorities);

        return accountDtoForGet;
    }

    public AccountDto accountDtoFromRegistrationDto(RegistrationDto dto){
        AccountDto account = new AccountDto();
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword1());
        return account;
    }
}
