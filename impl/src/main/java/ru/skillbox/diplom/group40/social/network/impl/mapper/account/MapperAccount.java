package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDtoForGet;
import ru.skillbox.diplom.group40.social.network.api.dto.account.Authorities;
import ru.skillbox.diplom.group40.social.network.api.dto.account.RolesDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;

import java.util.Optional;

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
        accountDtoForGet.setDeleted( account.isDeleted() );
        accountDtoForGet.setFirstName( account.getFirstName() );
        accountDtoForGet.setEmail( account.getEmail() );
        accountDtoForGet.setPassword( account.getPassword() );
        RolesDto[] rolesDtoArr = accountDtoForGet.getRoles();
        if(rolesDtoArr==null){
            rolesDtoArr = new RolesDto[2];
        }
        RolesDto rolesDto = new RolesDto();
        rolesDto.setId(account.getId());
        rolesDto.setRole("GAGE_ROLE");
        rolesDtoArr[0]=rolesDto;
        accountDtoForGet.setRoles(rolesDtoArr);

        Authorities[] authoritiesArr = new Authorities[2];
        if(authoritiesArr==null){
            authoritiesArr = new Authorities[2];
        }
        Authorities authorities = new Authorities();
        authorities.setId(account.getId());
        authorities.setAuthority("GAG_AUTHORITIES");
        authoritiesArr[0]=authorities;
        accountDtoForGet.setAuthorities(authoritiesArr);

        return accountDtoForGet;
    };
}
