package ru.skillbox.diplom.group40.social.network.impl.service.account;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group40.social.network.api.dto.adminConsole.StorageDto;
import ru.skillbox.diplom.group40.social.network.impl.repository.account.AccountRepository;

import java.io.*;
import java.util.Map;

@Log4j
@Service
@RequiredArgsConstructor
public class StorageServices {
    private final AccountRepository accountRepository;
    private int counterReqest=0;
    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    @Value("${cloudinary.api_key}")
    private String apiKey;
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    @Transactional
    public StorageDto create(MultipartFile path) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
        StorageDto storageDto = new StorageDto();
        String res="https://res.cloudinary.com/networkskillbox/image/upload/v1695848503/fz6g18cadewzujwqp7kp.jpg";
        if (path!= null) {
            Map uploadResult = cloudinary.uploader().upload(path.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            res = (String) uploadResult.get("secure_url");
        }
        storageDto.setFileName(res);
        return storageDto;
    }
}
