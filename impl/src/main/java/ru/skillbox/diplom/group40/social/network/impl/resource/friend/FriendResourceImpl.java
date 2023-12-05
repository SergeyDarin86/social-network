package ru.skillbox.diplom.group40.social.network.impl.resource.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendCountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.api.resource.dialogs.friend.FriendResource;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FriendResourceImpl implements FriendResource {

    private final FriendService friendService;

    @Override
    public ResponseEntity<FriendDto> create(UUID id) {
        return ResponseEntity.ok(friendService.create(id));
    }

    @Override
    public ResponseEntity<FriendDto> approve(UUID id)  {
        return ResponseEntity.ok(friendService.updateStatusCode(id, StatusCode.FRIEND));
    }

    @Override
    public ResponseEntity<String> delete(UUID id) {
        friendService.delete(id);
        return ResponseEntity.ok("");
    }

    @Override
    public ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto friendSearchDto, Pageable page) {
        return ResponseEntity.ok(friendService.getAll(friendSearchDto, page));
    }

    @Override
    public ResponseEntity<FriendDto> getById(UUID id) {
        return ResponseEntity.ok(friendService.getById(id));
    }

    @Override
    public ResponseEntity<List<String>> getByStatus(StatusCode status) {
        return ResponseEntity.ok(friendService.getAllByStatus(status));
    }

    @Override
    public ResponseEntity<List<FriendDto>> recommendations() {
        return ResponseEntity.ok(friendService.getRecommendations());
    }

    @Override
    public ResponseEntity<FriendDto> block(UUID id) {
        return ResponseEntity.ok(friendService.block(id));
    }

    @Override
    public ResponseEntity<FriendDto> unblock(UUID id) {
        return ResponseEntity.ok(friendService.unblock(id));
    }

    @Override
    public ResponseEntity<FriendDto> subscribe(UUID id) {
        return ResponseEntity.ok(friendService.createSubscribe(id));
    }

    @Override
    public ResponseEntity<FriendCountDto> count() {
        return ResponseEntity.ok(friendService.getCountRequestsFriend());
    }

    @Override
    public ResponseEntity<List<String>> getAllFriendsId() {
        return ResponseEntity.ok(friendService.getAllFriendsByCurrentUser());
    }

    @Override
    public ResponseEntity<List<String>> getAllFriendsIdById(UUID id) {
        return ResponseEntity.ok(friendService.getAllFriendsById(id));
    }

    @Override
    public ResponseEntity<Map<String, String>> getFriendsStatus(FriendSearchDto friendSearchDto) {
        return ResponseEntity.ok(friendService.getFriendsStatus(friendSearchDto.getIds()));
    }

    @Override
    public ResponseEntity<List<String>> getAllBlocked() {
        return ResponseEntity.ok(friendService.getAllBlocked());
    }

}
