package ru.skilllbox.diplom.group40.social.service.repository.accaunt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skilllbox.diplom.group40.social.service.model.account.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findByEmail(String email);
}
