package ru.skillbox.diplom.group40.social.network.impl.service.post;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.CommentRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CommentServiceTest {

    @Mock
    private CommentMapperImpl commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private CommentService commentService;

    @Before
    public void setUp() {
        commentService = new CommentService(commentMapper, commentRepository, likeService);
    }

    @Test
    void get() {
        UUID id = UUID.randomUUID();
        Comment comment = new Comment();
        comment.setId(id);
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.modelToDto(comment)).thenReturn(commentDto);
        CommentDto result = commentService.get(comment.getId());
        assertEquals(result, commentDto);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        Comment comment = new Comment();
        comment.setId(id);
        comment.setTimeChanged(ZonedDateTime.now());

        CommentDto commentDto = new CommentDto();
        commentDto.setTimeChanged(ZonedDateTime.now());

        when(commentRepository.findById(commentDto.getId())).thenReturn(Optional.of(comment));
        when(commentMapper.modelToDto(comment)).thenReturn(commentDto);

        CommentDto result = commentService.update(commentDto);
        assertEquals(result, commentDto);
    }

    @Test
    void getByAuthorIdAndTime() {
        UUID authorId = UUID.randomUUID();
        ZonedDateTime time = ZonedDateTime.now();
        Comment expected = new Comment();
        expected.setAuthorId(authorId);

        when(commentRepository.findByAuthorIdAndTime(expected.getAuthorId(), time)).thenReturn(expected);
        Comment actual = commentRepository.findByAuthorIdAndTime(expected.getAuthorId(), time);
        assertEquals(expected, actual);
    }


    @Test
    void getAllByPatentId() {
        Post post = new Post();
        post.setId(UUID.randomUUID());

        Comment comment = new Comment();
        comment.setParentId(post.getId());
        List<Comment> expected = new ArrayList<>();
        expected.add(comment);

        when(commentRepository.findAllByParentId(comment.getParentId())).thenReturn(expected);
        List<Comment> actual = commentService.getAllByPatentId(post.getId());
        assertEquals(expected, actual);
    }

}