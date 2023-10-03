package ru.skillbox.diplom.group40.social.network.impl.service.friend;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend_;
import ru.skillbox.diplom.group40.social.network.impl.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.friend.FriendRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;


import java.util.List;
import java.util.UUID;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.*;

@Log4j
@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    public FriendDto create(UUID id) {
        log.info("FriendService: create(UUID id), id = " + id + " (Start method)");
        if (friendRepository.findByAccountFromAndAccountTo(AuthUtil.getUserId(), id).isPresent()) {
            return new FriendDto();
        }
        Friend friend = new Friend(AuthUtil.getUserId(), id, StatusCode.REQUEST_TO, null, 0);
        friendRepository.save(friend);
        friendRepository.save(new Friend(id, AuthUtil.getUserId(), StatusCode.REQUEST_FROM, null,0));
        return friendMapper.toDto(friend);
    }

    public FriendDto createSubscribe(UUID id) {
        log.info("FriendService: createSubscribe(UUID id), id = " + id + " (Start method)");
        if (friendRepository.findByAccountFromAndAccountTo(AuthUtil.getUserId(), id).isPresent()) {
            return new FriendDto();
        }
        Friend friend = new Friend(AuthUtil.getUserId(), id, StatusCode.SUBSCRIBED, null, 0);
        friendRepository.save(friend);
        return friendMapper.toDto(friend);
    }

    public FriendDto updateStatusCode(UUID id, StatusCode statusCode) {
        log.info("FriendService: updateStatusCode(UUID id, StatusCode statusCode)" +
                ", id = " + id + ", statusCode = " + statusCode + " (Start method)");
        if (friendRepository.findByAccountFromAndAccountTo(AuthUtil.getUserId(), id).isEmpty()) {
            return new FriendDto();
        }
        Friend friend = updateStatusCodeEntity(AuthUtil.getUserId(), id, statusCode);
        updateStatusCodeEntity(id, AuthUtil.getUserId(), statusCode);
        return friendMapper.toDto(friend);
    }

    public void delete(UUID id) {
        log.info("FriendService: delete(UUID id), id = " + id + " (Start method)");
        deleteEntity(AuthUtil.getUserId(), id);
        deleteEntity(id, AuthUtil.getUserId());
    }

    public Page<FriendDto> getAll(FriendSearchDto friendSearchDto, Pageable page) {
        log.info("FriendService: getAll() Start method " + friendSearchDto);
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(friendSearchDto.getIsDeleted());
        Specification friendSpecification = getBaseSpecification(baseSearchDto)
                .and(in(Friend_.ACCOUNT_FROM, AuthUtil.getUserId()))
                .and(equal(Friend_.STATUS_CODE, friendSearchDto.getStatusCode()))
                .and(equal(Friend_.PREVIOUS_STATUS_CODE, friendSearchDto.getPreviousStatusCode()))
                .and(in(Friend_.ACCOUNT_TO, friendSearchDto.getId()))
                .and(equal(Friend_.RATING, friendSearchDto.getRating()));
        Page<Friend> friends = friendRepository.findAll(friendSpecification, page);

        return friends.map(friendMapper::toDto);
    }

    public List<UUID> getAllFriendsById(UUID id) {
        return friendRepository.findByAccountFrom(id).stream().map(Friend::getAccountTo).toList();
    }

    private void deleteEntity(UUID accountFrom, UUID accountTo) {
        Friend friend = friendRepository.findByAccountFromAndAccountTo(accountFrom, accountTo)
                .orElseThrow(EntityNotFoundException::new);
        friendRepository.deleteById(friend.getId());
    }
    private Friend updateStatusCodeEntity(UUID accountFrom, UUID accountTo, StatusCode statusCode) {
        Friend friend = friendRepository.findByAccountFromAndAccountTo(accountFrom, accountTo)
                .orElseThrow(EntityNotFoundException::new);
        friend.setPreviousStatusCode(friend.getStatusCode());
        friend.setStatusCode(statusCode);
        friendRepository.save(friend);
        return friend;
    }



}
