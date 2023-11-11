package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;

import java.util.HashMap;

public class HandlerContext {
    HashMap<Type, NotificationHandler> context = new HashMap<Type, NotificationHandler>();

        public void register(Type notificationType, NotificationHandler notificationHandler){
            context.put(notificationType, notificationHandler);
        }

        public NotificationHandler call(Type notificationType){
            return    context.get(notificationType);
        }

        public NotificationHandler get(Type notificationType){
            return context.get(notificationType);
        }



}
