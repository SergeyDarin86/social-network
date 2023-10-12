package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;

import java.util.UUID;

@Data
public class SocketNotificationDTO<T /*extends EvNotificationDTO&MessageDto*/> {

    private String type;
    private UUID recipientId;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private T data;

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    private EvNotificationDTO data;   //1  @Рабочее

//    Date date = new Date();      //      test
//    Date sentTime = new Date();      //      test


//StreamingMessageDto

//@Data
//@Schema(description = "Dto обертки сообщения")
//public class StreamingMessageDto<T> {
//
//    @Schema(description = "Тип вложенного сообщения (\"message\"/\"notification\")")
//    private String type;
//
//    @Schema(description = "UUID собеседника")
//    private UUID recipientId;
//
//    @Schema(description = "Dto сообщения")
//    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
//    private T data;
//
//}