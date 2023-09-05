package ru.skillbox.diplom.group40.social.network.impl.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.RegistrationDto;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.repository.accaunt.AccountRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.role.RoleRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountServices;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final AccountServices accountServices;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    public void testRegister(RegistrationDto loginDto) {    //это так не будет, просто проверка что сущности
        Account account = new Account();                    //корректно записываются
        account.setBlocked(false);
        account.setFirstName("Anton");
        account.setIsDeleted(true);
        Role adminRole = roleRepository.getByRole("ADMIN").orElse(null);
        Role userRole = roleRepository.getByRole("USER").orElse(null);

        if (adminRole != null && userRole != null) {
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            account.setRoles(roles);
        }
        accountRepository.save(account);
    }

    public void register(RegistrationDto loginDto) {
    }
}
