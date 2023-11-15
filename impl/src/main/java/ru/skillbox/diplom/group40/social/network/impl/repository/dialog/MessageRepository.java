package ru.skillbox.diplom.group40.social.network.impl.repository.dialog;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.group40.social.network.domain.account.Account;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.UUID;

import java.sql.Timestamp;

public interface MessageRepository extends BaseRepository<Message> {

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.readStatus = 'READ' WHERE m.dialogId = :dialogId AND m.readStatus = 'SENT'")
    void updateSentMessagesToRead(UUID dialogId);
    Message findTopByOrderByTimeDesc();
    @Query(value = "SELECT time FROM message ORDER BY time DESC NULLS LAST LIMIT 1;", nativeQuery = true)
    Timestamp findTopDate();
}
