package ru.skillbox.diplom.group40.social.network.api.resource.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api/v1/storage")
public interface StorageController {
    @PostMapping()
    public ResponseEntity<Object> putAvatar(@RequestParam String type, @RequestBody MultipartFile file);
}
