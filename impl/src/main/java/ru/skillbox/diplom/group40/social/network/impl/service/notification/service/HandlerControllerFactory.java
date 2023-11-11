package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;

public class HandlerControllerFactory {

    public static void HandlerController(NotificationDTO notificationDTO) {
/*
        switch (notificationDTO.getNotificationType()) {
            case LIKE:
                return new HandlerController(new Like(notificationDTO));
                break;
            case POST:
//                sendAllFriend(notificationDTO);
                break;
            case POST_COMMENT:
//                sendPostComment(notificationDTO);
                break;
            case COMMENT_COMMENT:
                sendCommentComment(notificationDTO);
                break;
            case MESSAGE:
                sendMessage(notificationDTO);
                break;
            case FRIEND_REQUEST:
                sendMeFriendRequest(notificationDTO);
                break;
            case FRIEND_BIRTHDAY:
                notificationDTO.setContent("");
                sendAllFriend(notificationDTO);
                break;
            case SEND_EMAIL_MESSAGE:
                sendEmail(notificationDTO);
                break;
            case FRIEND_APPROVE:
                sendMeFriendRequestApprove(notificationDTO);
                break;
            case FRIEND_BLOCKED:
                sendMeFriendBlocked(notificationDTO);
                break;
            case FRIEND_UNBLOCKED:
                sendMeFriendUnBlocked(notificationDTO);
                break;
            case FRIEND_SUBSCRIBE:
                sendMeFriendSubscribe(notificationDTO);
                break;
        }

        if(user.getTypeOfUser().equals(CUSTOMER)) {
            return new UserController(new CustomerService(user));
        } else if(user.getTypeOfUser().equals(DRIVER)) {
            return new UserController(new DriverService(user));
        }
        */
    }

}
