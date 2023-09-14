package ru.skillbox.diplom.group40.social.network.impl.repository.account;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Optional<List<Account>> findByEmail(String email);

    Optional<Account> findFirstByEmail(String email);

    Optional<List<Account>> findByEmailAndPassword(String email, String password);
}
