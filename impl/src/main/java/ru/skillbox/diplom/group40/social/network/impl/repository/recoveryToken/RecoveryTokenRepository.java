package ru.skillbox.diplom.group40.social.network.impl.repository.recoveryToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.passwordRecovery.RecoveryToken;

import java.util.UUID;
@Repository
public interface RecoveryTokenRepository extends JpaRepository<RecoveryToken, UUID> {
}
