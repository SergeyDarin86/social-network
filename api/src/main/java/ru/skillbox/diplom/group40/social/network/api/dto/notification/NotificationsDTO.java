package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

@Data
public class NotificationsDTO {

    int totalPages;
    int totalElements;
    int number;
    int size;
    ArrayList<ContentDTO> content;
    SortDTO sort;
    boolean first;
    boolean last;
    int numberOfElements;
    boolean empty;

}