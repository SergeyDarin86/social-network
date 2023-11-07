package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;
import ru.skillbox.diplom.group40.social.network.impl.utils.SpringUtils;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostComment {
    private PostService postService;
    private CommentService commentService;

    public UUID getPostComment(NotificationDTO notificationDTO) {
        log.info("PostComment: getPostComment(NotificationDTO notificationDTO) startMethod");

        /** Внедрение зависимостей из контекста */
        postService = (PostService) SpringUtils.ctx.getBean(PostService.class);
        commentService = (CommentService) SpringUtils.ctx.getBean(CommentService.class);
        /** */

        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime().toLocalDateTime());

        PostDto postDto = postService.get(comment.getPostId());
        UUID accountId = postDto.getAuthorId();

        log.info("PostComment: getPostComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

        return accountId;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
        public PageWriter(Site site) throws InterruptedException {
        this.site = site;
        pageRepository = (PageRepository) SpringUtils.ctx.getBean(PageRepository.class);
    */