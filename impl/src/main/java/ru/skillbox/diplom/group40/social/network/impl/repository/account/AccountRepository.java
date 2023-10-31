package ru.skillbox.diplom.group40.social.network.impl.repository.account;


import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation.Metric;

import java.util.Optional;


@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Optional<Account> findFirstByEmail(String email);
}
