package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class NotificationDTO extends BaseDto {

    UUID authorId;
    String content;
    Type notificationType;
    ZonedDateTime sentTime;

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OfferAllocationConstants.ISO_DATE_FORMAT)
//    @JsonProperty("$date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//    LocalDateTime sentTime;