package ru.skillbox.diplom.group40.social.network.impl.resource.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.api.resource.friend.FriendResource;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;

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
    public ResponseEntity<FriendDto> approve(UUID id) {
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
    public ResponseEntity<Page<FriendDto>> recommendations(FriendSearchDto friendSearchDto, Pageable page) {
        return ResponseEntity.ok(friendService.getAll(friendSearchDto, page));
    }

    @Override
    public ResponseEntity<FriendDto> block(UUID id) {
        return ResponseEntity.ok(friendService.updateStatusCode(id, StatusCode.BLOCKED));
    }

    @Override
    public ResponseEntity<FriendDto> unblock(UUID id) {
        return ResponseEntity.ok(friendService.updateStatusCode(id, StatusCode.FRIEND));
    }

    @Override
    public ResponseEntity<FriendDto> subscribe(UUID id) {
        return ResponseEntity.ok(friendService.createSubscribe(id));
    }

}
