package ru.skillbox.diplom.group40.social.network.impl.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group40.social.network.domain.role.Role;
import ru.skillbox.diplom.group40.social.network.impl.repository.role.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Set<Role> getRoleSet(List<String> roleNames){
        Set<Role> roles = new HashSet<>();
        for(String roleName : roleNames){
            roles.add(roleRepository.getByRole(roleName).orElse(null));
        }
        return roles;
    }
}
