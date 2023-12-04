package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;

import java.util.UUID;

@Data
@Schema(description = "DTO для поиска информации по комментариям")
public class CommentSearchDto extends BaseSearchDto {

    @Schema(description = "Тип комментария (к комментарию или к посту)")
    private CommentType commentType;

    @Schema(description = "Id автора комментария")
    private UUID authorId;

    @Schema(description = "Id родительского объекта, к которому оставили комментарий (пост или комментарий)")
    private UUID parentId;

    @Schema(description = "Id поста")
    private UUID postId;

    @Schema(description = "Id комментария")
    private UUID commentId;
}
