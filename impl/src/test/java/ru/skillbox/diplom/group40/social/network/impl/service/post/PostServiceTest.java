package ru.skillbox.diplom.group40.social.network.impl.service.post;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;

import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class PostServiceTest {

    @Mock
    private PostMapper postMapper;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private LikeService likeService;
    @Mock
    private AccountService accountService;
    @Mock
    private NotificationsMapper notificationsMapper;
    @Mock
    private KafkaService kafkaService;

    @InjectMocks
    private PostService postService;

    @Before
    public void setUp() {
        postService = new PostService(postMapper, postRepository, commentService, commentMapper
                , likeService, accountService, notificationsMapper, kafkaService);
    }

    @Test
    void update() {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setTitle("Hello");
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle("Hi");

        when(postRepository.findById(postDto.getId())).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);
        when(postRepository.save(postMapper.toPost(postDto))).thenReturn(post);

        PostDto result = postService.update(postDto);
        assertEquals(result, postDto);
    }

    @Test
    void get() {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setTitle("Hello");
        PostDto postDto = new PostDto();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);
        PostDto result = postService.get(post.getId());
        assertEquals(result, postDto);
    }

    @Test
    void deleteById() {
        UUID uuid = UUID.randomUUID();
        Post post = new Post();
        post.setId(uuid);

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> postService.deleteById(uuid))
                .withMessage("Пользователь не найден");
    }
}