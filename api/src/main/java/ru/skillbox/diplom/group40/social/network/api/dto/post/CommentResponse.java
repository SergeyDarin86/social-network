package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;

import java.util.UUID;
@Data
public class CommentResponse {
    private UUID parentId;
    private String commentText;
}
