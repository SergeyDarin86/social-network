package ru.skillbox.diplom.group40.social.network.impl.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<List<Account>> findByEmail(String email);

    Optional<List<Account>> findFirstByEmail(String email);

    Optional<List<Account>> findByEmailAndPassword(String email, String password);
}
