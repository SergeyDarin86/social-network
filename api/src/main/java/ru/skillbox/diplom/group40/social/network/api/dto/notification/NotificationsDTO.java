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
    Pageable pageable;   //TODO убрать
    boolean empty;

}

    /*
    {
      "totalPages": 0,
      "totalElements": 0,
      "number": 0,
      "size": 0,
      "content": [
                    {
                      "timeStamp": "2023-09-21T09:02:49.993Z",
                      "data": {
                                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                "authorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                "content": "string",
                                "notificationType": "string",
                                "sentTime": "2023-09-21T09:02:49.993Z"
                              }
                    }
                ],
      "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": true
              },
      "first": true,
      "last": true,
      "numberOfElements": 0,
      "pageable": {
                      "offset": 0,
                      "sort": {
                                "empty": true,
                                "unsorted": true,
                                "sorted": true
                              },
                      "paged": true,
                      "pageSize": 0,
                      "unpaged": true,
                      "pageNumber": 0
                  },
      "empty": true
    }
    */