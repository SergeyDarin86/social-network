package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO для поиска информации по постам")
public class PostSearchDto extends BaseDto {

    @Schema(description = "Список id-шников постов")
    private List<UUID> ids;

    @Schema(description = "Список id-шников авторов")
    private List<UUID> accountIds;

    @Schema(description = "Список заблокированных id-шников авторов")
    private List<UUID> blockedIds;

    @Schema(description = "Автор поста", defaultValue = "Сергей")
    private String author;

    @Schema(description = "Текст поста, его содержимое", defaultValue = "содержимое поста")
    private String text;

    @Schema(description = "Статус автора поста (с друзьями или нет)")
    private Boolean withFriends;

    @Schema(description = "Нижняя граница диапазона даты публикации поста")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateFrom;

    @Schema(description = "Верхняя граница диапазона даты публикации поста")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTo;

    @Schema(description = "Список тегов к посту")
    private List<String> tags;

}
