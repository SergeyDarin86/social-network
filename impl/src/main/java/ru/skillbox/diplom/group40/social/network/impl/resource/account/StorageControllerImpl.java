package ru.skillbox.diplom.group40.social.network.impl.resource.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group40.social.network.api.dto.adminConsole.StorageDto;
import ru.skillbox.diplom.group40.social.network.api.resource.account.StorageController;
import ru.skillbox.diplom.group40.social.network.impl.service.account.StorageServices;

import java.io.IOException;

@Log4j
@RestController
@RequiredArgsConstructor
public class StorageControllerImpl implements StorageController {
private final StorageServices storageServices;

    public ResponseEntity<StorageDto> putAvatar(@RequestBody MultipartFile file){
    try {
        return ResponseEntity.ok(storageServices.create(file));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }
}
