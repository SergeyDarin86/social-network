package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;

import java.util.UUID;

@Data
public class CommentSearchDto extends BaseSearchDto {
    private CommentType commentType;
    private UUID authorId;
    private UUID parentId;
    private UUID postId;
    private UUID commentId;
}
