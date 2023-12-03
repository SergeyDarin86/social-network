package ru.skillbox.diplom.group40.social.network.impl.repository.dialog;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends BaseRepository<Message> {

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.readStatus = 'READ' WHERE m.dialogId = :dialogId AND m.readStatus = 'SENT'")
    void updateSentMessagesToRead(UUID dialogId);

    @Query(value = "SELECT time FROM message ORDER BY time DESC NULLS LAST LIMIT 1;", nativeQuery = true)
    Optional<Timestamp> findTopDate();
}
