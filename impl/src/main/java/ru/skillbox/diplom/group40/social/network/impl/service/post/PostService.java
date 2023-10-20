package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.domain.post.Post_;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.UUID;


/**
 * PostService
 *
 * @author Sergey D.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AccountService accountService;

    private final NotificationsMapper notificationsMapper;
    private final KafkaService kafkaService;                   /** @Рабочее - Использовать для кафки   */                                                  //    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;
    private final NotificationService notificationService;     /**  Использовать при отключенной кафке  */
    private String notFoundMessage = "Пользователь не найден";

    @jakarta.transaction.Transactional()
    public PostDto create(PostDto postDto) {
        log.info("PostService: save(PostDto postDto), title = " + postDto.getTitle() + " (Start method");
        postDto.setAuthorId(AuthUtil.getUserId());
        createNotification(postDto);
        return postMapper.toDto(postRepository.save(postMapper.toPostForCreate(postDto)));
    }

    public PostDto update(PostDto postDto) {
        log.info("PostService: update(PostDto postDto), id = " + postDto.getId() + " (Start method)");
        Post currentPost = postRepository.findById(postDto.getId()).orElseThrow(()
                -> new NotFoundException(notFoundMessage));

        return postMapper.toDto(postRepository.save(postMapper.toPostForUpdate(postDto, currentPost)));
    }

    public PostDto get(UUID id) {
        log.info("PostService: get(UUID id), id = " + id + " (Start method)");
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(notFoundMessage)));
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException {
        log.info("PostService: getAll() Start method " + postSearchDto);

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(postSearchDto.getIsDeleted());

        Specification postDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Post_.AUTHOR_ID, postSearchDto.getAccountIds()))
                .and(SpecificationUtils.in(Post_.ID, postSearchDto.getIds()))
                .and(SpecificationUtils.like(Post_.POST_TEXT, postSearchDto.getText()))
                .and(SpecificationUtils.betweenDate(Post_.PUBLISH_DATE, postSearchDto.getDateFrom(), postSearchDto.getDateTo()))
                .and(SpecificationUtils.in(Post_.AUTHOR_ID, uuidListFromAccount(postSearchDto)));

        Page<Post> posts = postRepository.findAll(postDtoSpecification, page);

        return posts.map(postMapper::toDto);
    }

    public List<UUID> uuidListFromAccount(PostSearchDto postSearchDto) throws AccountException {
        AccountSearchDto accountSearchDto = new AccountSearchDto();
        accountSearchDto.setFirstName(postSearchDto.getAuthor());
        accountSearchDto.setLastName(postSearchDto.getAuthor());
        Page<AccountDto> accounts = accountService.getAll(accountSearchDto, Pageable.unpaged());

        return accounts.stream().map(BaseDto::getId).toList();
    }

    public void deleteById(UUID id) {
        log.info("PostService: deleteById(PostDto postDto), id = " + id + " (Start method)");
        postRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage));
        postRepository.deleteById(id);
    }
    public void createNotification(PostDto postDto) {
        log.info("PostService: createNotification(Post post) startMethod, id = {}", postDto.getId());
        kafkaService.sendNotification(notificationsMapper.postToNotificationDTO(postDto));  /** @Рабочее - Использовать для кафки   */
//        notificationService.create(notificationsMapper.postToNotificationDTO(postDto));     /**  Использовать при отключенной кафке  */
    }
}
