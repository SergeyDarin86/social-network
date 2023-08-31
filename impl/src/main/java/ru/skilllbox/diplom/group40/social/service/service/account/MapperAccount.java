package ru.skilllbox.diplom.group40.social.service.service.account;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skilllbox.diplom.group40.social.service.dto.account.AccountDto;
import ru.skilllbox.diplom.group40.social.service.dto.account.AccountDtoForGet;
import ru.skilllbox.diplom.group40.social.service.dto.account.Authorities;
import ru.skilllbox.diplom.group40.social.service.dto.account.RolesDto;
import ru.skilllbox.diplom.group40.social.service.model.account.Account;
@Component
@Mapper(componentModel = "spring")
public abstract class MapperAccount {

    abstract AccountDto toDto(Account account);
    abstract Account toEntity(AccountDto accountDto);

    AccountDtoForGet  toDtoForGet(Account account){
        if ( account == null ) {
            return null;
        }

        AccountDtoForGet accountDtoForGet = new AccountDtoForGet();
        accountDtoForGet.setId( account.getId() );
        accountDtoForGet.setDeleted( account.isDeleted() );
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
    };
}
