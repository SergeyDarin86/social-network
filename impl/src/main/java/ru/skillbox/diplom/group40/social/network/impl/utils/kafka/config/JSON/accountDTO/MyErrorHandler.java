package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON.accountDTO;

import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

public interface MyErrorHandler extends CommonErrorHandler {

    default void handleOtherException(Exception thrownException, Consumer<?, ?> consumer,
                                      MessageListenerContainer container, boolean batchListener) {
        LogFactory.getLog(getClass()).error("'handleOtherException' is not implemented by this handler",
                thrownException);
    }

    default String myHandleOtherException(Exception thrownException, Consumer<?, ?> consumer,
                                      MessageListenerContainer container, boolean batchListener) {
        handleOtherException(thrownException, consumer, container, batchListener);
        return  LogFactory.getLog(getClass()).toString();
    }
}
