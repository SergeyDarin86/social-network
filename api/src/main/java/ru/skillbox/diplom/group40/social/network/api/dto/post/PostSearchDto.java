package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * PostSearchDto
 *
 * @author Sergey D.
 */

@Data
public class PostSearchDto extends BaseDto {

    private List<UUID> ids;

    private List<UUID> accountIds;

    private List<UUID> blockedIds;

    private String author;

    private String text;

    private Boolean withFriends;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTo;

}
