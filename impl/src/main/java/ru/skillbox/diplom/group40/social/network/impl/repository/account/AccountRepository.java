package ru.skillbox.diplom.group40.social.network.impl.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Optional<Account> findFirstByEmail(String email);
//    Account findFirstByLastOnlineTimeOrderByLastOnlineTimeAsc();
//    Account findMaxLastOnlineTime();

//    Account getAccountMaxLastOnlineTime();

//    Account findByLastOnlineTimeOrderByLastOnlineTimeAsc();
//    Account findTop1OrderByLastOnlineTimeAsc(); // @Полурабочее, LDT problems
    Account findTopByOrderByLastOnlineTimeAsc();
    Account findTopByOrderByLastOnlineTimeDesc();
}
