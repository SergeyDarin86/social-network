package ru.skillbox.diplom.group40.social.network.impl.service.friend;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendCountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend_;
import ru.skillbox.diplom.group40.social.network.impl.mapper.friend.FriendMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.friend.FriendRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final KafkaService kafkaService;

    public FriendDto create(UUID id) {
        log.info("FriendService: create(UUID id), id = " + id + " (Start method)");
        Friend friend = createFriendEntity(AuthUtil.getUserId(), id, StatusCode.REQUEST_TO);
        sendNotification(friend);
        createFriendEntity(id, AuthUtil.getUserId(), StatusCode.REQUEST_FROM);
        return friendMapper.toDto(friend);
    }

    public FriendDto createSubscribe(UUID id) {
        log.info("FriendService: createSubscribe(UUID id), id = " + id + " (Start method)");
        Friend friend = createFriendEntity(AuthUtil.getUserId(), id, StatusCode.WATCHING);
        createFriendEntity(id, AuthUtil.getUserId(), StatusCode.SUBSCRIBED);
        return friendMapper.toDto(friend);
    }

    public FriendDto updateStatusCode(UUID id, StatusCode statusCode) {
        log.info("FriendService: updateStatusCode(UUID id, StatusCode statusCode)" +
                ", id = " + id + ", statusCode = " + statusCode + " (Start method)");
        if (friendRepository.findByAccountFromAndAccountTo(AuthUtil.getUserId(), id).isEmpty()) {
            return new FriendDto();
        }
        Friend friend = updateFriendStatusCodeEntity(AuthUtil.getUserId(), id, statusCode);
        sendNotification(friend);
        updateFriendStatusCodeEntity(id, AuthUtil.getUserId(), statusCode);
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
        baseSearchDto.setIsDeleted(false);
        Specification friendSpecification = getBaseSpecification(baseSearchDto)
                .and(equal(Friend_.ACCOUNT_FROM, AuthUtil.getUserId()))
                .and(equal(Friend_.STATUS_CODE, friendSearchDto.getStatusCode()))
                .and(equal(Friend_.PREVIOUS_STATUS_CODE, friendSearchDto.getPreviousStatusCode()))
                .and(equal(Friend_.ACCOUNT_TO, friendSearchDto.getIdTo()));
        Page<Friend> friends = friendRepository.findAll(friendSpecification, page);

        return friends.map(friendMapper::toDto);
    }

    public List<FriendDto> getRecommendations() {
        log.info("FriendService: getRecommendations(), (Start method)");
        return friendRepository.findAllOrderedByNumberFriends(AuthUtil.getUserId()).stream()
                .map(objects -> new FriendDto((UUID) objects[0], Math.toIntExact((Long) objects[1]))).toList();
    }

    public List<String> getAllFriendsByCurrentUser() {
        log.info("FriendService: getAllFriendsByCurrentUser(), (Start method)");
        return getAllFriendsUuids(AuthUtil.getUserId(), StatusCode.FRIEND);
    }

    public List<String> getAllFriendsById(UUID id) {
        log.info("FriendService: getAllFriendsById(UUID id), id = " + id + " (Start method)");
        return getAllFriendsUuids(id, StatusCode.FRIEND);
    }

    public List<String> getAllByStatus(StatusCode status) {
        log.info("FriendService: getAllByStatus(StatusCode status)" +
                ", status = " + status + ", (Start method)");
        return getAllFriendsUuids(AuthUtil.getUserId(), status);
    }

    public FriendDto getById(UUID id) {
        log.info("FriendService: getById(UUID id), id = " + id + " (Start method)");
        return friendMapper.toDto(friendRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    public List<String> getAllBlocked() {
        log.info("FriendService: getAllBlocked(), (Start method)");
        ArrayList<StatusCode> statusCodes = new ArrayList<>();
        statusCodes.add(StatusCode.BLOCKED);
        statusCodes.add(StatusCode.NONE);
        return friendRepository.findByAccountToAndStatusCodeInAndIsDeletedFalse(AuthUtil.getUserId(), statusCodes)
                .stream().map(friend -> friend.getAccountFrom().toString()).toList();
    }

    public List<String> getAllFriendsUuids(UUID id, StatusCode status) {
        return friendRepository.findByAccountFromAndStatusCodeAndIsDeletedFalse(id, status)
                .stream().map(friend -> friend.getAccountTo().toString()).toList();
    }

    private Friend createFriendEntity(UUID accountFrom, UUID accountTo, StatusCode statusCode) {
        Friend friend = friendRepository.findByAccountFromAndAccountTo(accountFrom, accountTo)
                .orElse(new Friend(accountFrom, accountTo, statusCode, null));
        friend.setIsDeleted(false);
        friend.setStatusCode(statusCode);
        friendRepository.save(friend);
        return friend;
    }

    private void sendNotification(Friend friend) {
        if (friend.getStatusCode() != StatusCode.REQUEST_TO
            && friend.getStatusCode() != StatusCode.FRIEND) {
            return;
        }
        kafkaService.sendNotification(friendMapper.toNotificationDTO(friend));
    }

    private void deleteEntity(UUID accountFrom, UUID accountTo) {
        Friend friend = friendRepository.findByAccountFromAndAccountTo(accountFrom, accountTo)
                .orElseThrow(EntityNotFoundException::new);
        friendRepository.deleteById(friend.getId());
    }

    private Friend updateFriendStatusCodeEntity(UUID accountFrom, UUID accountTo, StatusCode statusCode) {
        Friend friend = friendRepository.findByAccountFromAndAccountTo(accountFrom, accountTo)
                .orElse(new Friend(accountFrom, accountTo, null, null));
        if (statusCode == null
            && friend.getPreviousStatusCode() == null) {
            // для разблокировки если до блокировки отношений не было
            friendRepository.deleteById(friend.getId());
            return friend;
        }
        friend.setIsDeleted(false);
        StatusCode previousStatusCode = friend.getPreviousStatusCode();
        friend.setPreviousStatusCode((statusCode == StatusCode.BLOCKED
                    || statusCode == StatusCode.NONE) ? friend.getStatusCode() : null);
        friend.setStatusCode(statusCode == null ? previousStatusCode : statusCode);
        friendRepository.save(friend);
        return friend;
    }

    public FriendDto block(UUID id) {
        log.info("FriendService: block(UUID id), id = " + id + " (Start method)");
        Friend friend = updateFriendStatusCodeEntity(AuthUtil.getUserId(), id, StatusCode.BLOCKED);
        updateFriendStatusCodeEntity(id, AuthUtil.getUserId(), StatusCode.NONE);
        return friendMapper.toDto(friend);
    }

    public FriendDto unblock(UUID id) {
        log.info("FriendService: unblock(UUID id)" +
                ", id = " + id + ", (Start method)");
        Friend friend = updateFriendStatusCodeEntity(AuthUtil.getUserId(), id, null);
        updateFriendStatusCodeEntity(id, AuthUtil.getUserId(), null);
        return friendMapper.toDto(friend);
    }

    public FriendCountDto getCountFriends() {
        log.info("FriendService: getCountFriends(), (Start method)");
        return new FriendCountDto(friendRepository.countByAccountFromAndStatusCodeAndIsDeleted(AuthUtil.getUserId()
                , StatusCode.FRIEND, false));
    }

    public Map<String, String> getFriendsStatus(List<UUID> ids) {
        log.info("FriendService: getFriendsStatus(), (Start method)");
        return friendRepository.findAllByAccountToInAndAccountFromAndIsDeletedFalse(ids, AuthUtil.getUserId()).stream()
                .collect(Collectors.toMap(f -> String.valueOf(f.getAccountTo()), f -> f.getStatusCode().toString()));
    }

}
