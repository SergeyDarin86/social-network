package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.config.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;

public class CustomJsonDeserializer extends JsonDeserializer<NotificationDTO> {


    public static final ObjectMapper objectmapper = createMapper();

    public static final ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;

    }

    public CustomJsonDeserializer() {}

}
