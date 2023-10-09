package ru.skillbox.diplom.group40.social.network.api.resource.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendSearchDto;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/friends")
public interface FriendResource {

    @PostMapping("/{id}/request")
    ResponseEntity<FriendDto> create(@PathVariable UUID id);

    @PutMapping("/{id}/approve")
    ResponseEntity<FriendDto> approve(@PathVariable UUID id);

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable UUID id);

    @GetMapping()
    ResponseEntity<Page<FriendDto>> getAll(FriendSearchDto friendSearchDto, Pageable page);

    @GetMapping("/recommendations")
    ResponseEntity<List<FriendDto>> recommendations();

    @PutMapping("/block/{id}")
    ResponseEntity<FriendDto> block(@PathVariable UUID id);

    @PutMapping("/unblock/{id}")
    ResponseEntity<FriendDto> unblock(@PathVariable UUID id);

    @PostMapping("/subscribe/{id}")
    ResponseEntity<FriendDto> subscribe(@PathVariable UUID id);
}
