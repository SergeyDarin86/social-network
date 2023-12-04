package ru.skillbox.diplom.group40.social.network.impl.service.post;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.LikeMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.LikeRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class LikeServiceTest {

    @Mock
    private LikeMapper likeMapper;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private KafkaService kafkaService;

    @Mock
    private NotificationsMapper notificationsMapper;

    @InjectMocks
    private LikeService likeService;

    @Before
    public void setUp() {
        likeService = new LikeService(likeMapper, likeRepository, kafkaService, notificationsMapper);
    }

    @Test
    void getAllByItemId() {
        UUID itemId = UUID.randomUUID();
        List<Like> expected = new ArrayList<>();
        Like like = new Like();
        like.setItemId(itemId);
        expected.add(like);

        when(likeRepository.findAllByItemId(itemId)).thenReturn(expected);
        List<Like> actual = likeService.getAllByItemId(itemId);
        assertEquals(expected, actual);
    }

    @Test
    void getLike() {
        UUID likeId = UUID.randomUUID();
        Like expected = new Like();
        expected.setId(likeId);

        when(likeRepository.findById(likeId)).thenReturn(Optional.of(expected));
        Like actual = likeService.getLike(likeId);
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        UUID likeId = UUID.randomUUID();
        Like like = new Like();
        like.setId(likeId);
        when(likeRepository.save(like)).thenReturn(like);
        likeService.update(like);
    }

}