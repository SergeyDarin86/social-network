package ru.skillbox.diplom.group40.social.network.impl.mapper.notification;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.EventNotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

@Component
@Mapper(componentModel = "spring")
public interface NotificationMapper extends BaseMapper {

    EventNotificationDTO modelToDto(EventNotification eventNotification);
    EventNotification dtoToModel(EventNotificationDTO eventNotificationDTO);

}
