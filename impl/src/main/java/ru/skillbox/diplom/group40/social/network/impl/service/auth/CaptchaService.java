package ru.skillbox.diplom.group40.social.network.impl.service.auth;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.CaptchaDto;
import ru.skillbox.diplom.group40.social.network.domain.captcha.CaptchaEntity;
import ru.skillbox.diplom.group40.social.network.impl.config.captcha.CustomTextProducer;
import ru.skillbox.diplom.group40.social.network.impl.repository.captcha.CaptchaRepository;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.SecureDirectoryStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class CaptchaService {
    private final CaptchaRepository captchaRepository;
    private final CustomTextProducer textProducer;

    public CaptchaDto getCaptcha() {
        Captcha captcha = generateCaptcha(200, 60);
        CaptchaDto captchaDto = new CaptchaDto();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(captcha.getImage(), "jpg", os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String IMAGE_FORMAT = "jpg";
        String prefix = "data:image/" + IMAGE_FORMAT + ";base64, ";
        captchaDto.setImage(prefix + Base64.getEncoder().encodeToString(os.toByteArray()));
        CaptchaEntity captchaEntity = new CaptchaEntity();
        captchaEntity.setAnswer(captcha.getAnswer());
        captchaEntity.setExpirationTime(LocalDateTime.now().plus(15, ChronoUnit.MINUTES));
        captchaRepository.save(captchaEntity);
        captchaDto.setSecret(captchaEntity.getId().toString());
        return captchaDto;
    }

    public Captcha generateCaptcha(Integer width, Integer height) {
        return new Captcha.Builder(width, height).addBackground(new GradiatedBackgroundProducer())
                .addText(textProducer, new DefaultWordRenderer()).addNoise(new CurvedLineNoiseProducer())
                .build();
    }

    public boolean captchaIsCorrect(String captchaCode, String captchaSecret) {
        CaptchaEntity captcha = captchaRepository.getReferenceById(UUID.fromString(captchaSecret));
        if (captcha.getExpirationTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        return captchaCode.equals(captcha.getAnswer());
    }
}
