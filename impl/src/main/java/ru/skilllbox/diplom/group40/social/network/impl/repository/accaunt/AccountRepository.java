package ru.skilllbox.diplom.group40.social.network.impl.repository.accaunt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skilllbox.diplom.group40.social.network.domain.account.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findByEmail(String email);
}
