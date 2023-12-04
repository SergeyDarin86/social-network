package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.post.*;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.LikeMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.LikeRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;

    private final KafkaService kafkaService;

    private final NotificationsMapper notificationsMapper;

    public LikeDto createForComment(UUID id, UUID commentId){
        log.info("LikeService: create like for comment with id: " + commentId);

        LikeDto likeDto = new LikeDto();
        likeDto.setIsDeleted(false);
        likeDto.setAuthorId(AuthUtil.getUserId());
        likeDto.setTime(ZonedDateTime.now());
        likeDto.setItemId(commentId);
        likeDto.setType(LikeType.COMMENT);
        createNotificationForLike(likeRepository.save(likeMapper.dtoToModel(likeDto)));

        return likeDto;
    }
    public void deleteForComment(UUID id, UUID commentId){
        log.info("LikeService: delete like for comment with id: " + commentId);

        List<Like> likes = getByAuthorAndItem(AuthUtil.getUserId(), commentId);
        for(Like like : likes){
            if(!like.getIsDeleted()){
                like.setIsDeleted(true);
                likeRepository.save(like);
            }
        }
    }
    public LikeDto createForPost(UUID id, LikeDto response){
        log.info("LikeService: create like for post with id: " + id);

        LikeDto likeDto = new LikeDto();
        likeDto.setIsDeleted(false);
        likeDto.setAuthorId(AuthUtil.getUserId());
        likeDto.setTime(ZonedDateTime.now());
        likeDto.setItemId(id);
        likeDto.setType(LikeType.POST);
        likeDto.setReactionType(response.getReactionType());
        createNotificationForLike(likeRepository.save(likeMapper.dtoToModel(likeDto)));
        return likeDto;
    }

    public void deleteForPost(UUID id){
        log.info("LikeService: delete like for post with id: " + id);

        List<Like> likes = getByAuthorAndItem(AuthUtil.getUserId(), id);
        for(Like like : likes){
            if(!like.getIsDeleted()){
                like.setIsDeleted(true);
                likeRepository.save(like);
            }
        }
    }

    public List<Like> getByAuthorAndItem(UUID authorId, UUID itemId){
        log.info("LikeService: get likes by author with id: " + authorId + " and item id: " + itemId );
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        Specification likeSpec = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Like_.AUTHOR_ID, authorId))
                .and(SpecificationUtils.in(Like_.ITEM_ID, itemId));
        List<Like> likeOptional = likeRepository.findAll(likeSpec);
        return likeOptional;
    }

    public List<Like> getAllByItemId(UUID itemId){
        return likeRepository.findAllByItemId(itemId);
    }

    public Like getLike(UUID likeId) {
        log.info("LikeService: get(UUID likeId) startMethod, likeId = : " + likeId );
        Like like = likeRepository.findById(likeId).get();
        return like;
    }

    public void update(Like like){
        likeRepository.save(like);
    }

    public void createNotificationForLike(Like like) {
        log.info("PostService: createNotificationForLike(LikeDto likeDto) startMethod, ", like);
        kafkaService.sendNotification(notificationsMapper.likeToNotificationDTO(like));
    }

}
