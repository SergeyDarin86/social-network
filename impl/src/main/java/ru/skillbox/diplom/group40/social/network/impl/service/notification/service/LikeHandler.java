package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.LikeService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;
import ru.skillbox.diplom.group40.social.network.impl.utils.SpringUtils;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeHandler {
    private LikeService likeService;
    private CommentService commentService;
    private PostService postService;
    private static final String SEND_LIKE_POST = "поставил LIKE на Вашу запись \"";
    private static final String SEND_LIKE_COMMENT = "поставил LIKE на Ваш комментарий \"";

    public UUID getLike(NotificationDTO notificationDTO) {

        /** Внедрение зависимостей из контекста */
        postService = (PostService) SpringUtils.ctx.getBean(PostService.class);
        commentService = (CommentService) SpringUtils.ctx.getBean(CommentService.class);
        likeService = (LikeService) SpringUtils.ctx.getBean(LikeService.class);
        /** */

        log.info("Like: getLike(NotificationDTO notificationDTO) startMethod, NotificationDTO = {}",
                notificationDTO);
        UUID likeId = notificationDTO.getAuthorId();
        ru.skillbox.diplom.group40.social.network.domain.post.Like like = likeService.getLike(likeId);
        UUID accountId = null;

        if (like.getType().equals(LikeType.POST)) {
            /*
            Post post = (Post) postRepository.findById(like.getItemId()).orElseThrow(()
                    -> new NotFoundException("notFoundPostMessage"));
            accountId = post.getAuthorId();
            */
            PostDto postDto = postService.get(like.getItemId());
            accountId = postDto.getAuthorId();

            notificationDTO.setContent(SEND_LIKE_POST.concat(postDto.getPostText().concat("\"")));
        }

        if (like.getType().equals(LikeType.COMMENT)) {
            CommentDto commentDto = commentService.get(like.getItemId());
            accountId = commentDto.getAuthorId();
            log.info("Like: getLike(NotificationDTO notificationDTO) получен UUID автора COMMENT'а: {}",
                    accountId);
            notificationDTO.setContent(SEND_LIKE_COMMENT.concat(commentDto.getCommentText()).concat("\""));
        }

        UUID authorId = like.getAuthorId();
        notificationDTO.setAuthorId(authorId);

//        socketSendOneUser(notificationDTO, accountId);

        return accountId;
    }

}
