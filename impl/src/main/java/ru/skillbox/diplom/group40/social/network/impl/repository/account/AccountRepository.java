package ru.skillbox.diplom.group40.social.network.impl.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation.Metric;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends BaseRepository<Account> {

    Optional<Account> findFirstByEmail(String email);

    @Query(value = """
            SELECT id AS id FROM account 
            WHERE extract(MONTH FROM birth_date) = :m 
            AND extract(DAY FROM birth_date) = :d""",
            nativeQuery = true)
    List<Object[]> findAllByBirthDate(@Param("d") int dayOfBirth, @Param("m") int monthOfBirth);

}
