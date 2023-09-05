package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.post.CommentEntity;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServices {
    private final CommentMapperImpl commentMapper;
    private final CommentRepository commentRepository;

    public CommentDto update(CommentDto commentDto){
        CommentEntity commentEntity = commentRepository.findById(commentDto.getId()).get();
        commentEntity = commentRepository.save(commentMapper.dtoToModel(commentDto));
        return commentMapper.modelToDto(commentEntity);
    }

    public CommentDto save(CommentDto commentDto){
        CommentEntity commentEntity = commentMapper.dtoToModel(commentDto);
        commentEntity = commentRepository.save(commentEntity);
        return commentMapper.modelToDto(commentEntity);
    }


}
