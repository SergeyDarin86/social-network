package ru.skillbox.diplom.group40.social.network.impl.service.auth;

import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.CaptchaDto;
import ru.skillbox.diplom.group40.social.network.domain.captcha.Captcha;
import ru.skillbox.diplom.group40.social.network.impl.config.captcha.CustomTextProducer;
import ru.skillbox.diplom.group40.social.network.impl.exception.AuthException;
import ru.skillbox.diplom.group40.social.network.impl.repository.captcha.CaptchaRepository;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
public class CaptchaService {
    private final CaptchaRepository captchaRepository;
    private final CustomTextProducer textProducer;

    public CaptchaDto getCaptcha() {
        cn.apiclub.captcha.Captcha captcha = generateCaptcha(200, 60);
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
        Captcha captchaEntity = new Captcha();
        captchaEntity.setAnswer(captcha.getAnswer());
        captchaEntity.setExpirationTime(ZonedDateTime.now().plus(15, ChronoUnit.MINUTES));
        captchaRepository.save(captchaEntity);
        captchaDto.setSecret(captchaEntity.getId().toString());
        return captchaDto;
    }

    public cn.apiclub.captcha.Captcha generateCaptcha(Integer width, Integer height) {
        return new cn.apiclub.captcha.Captcha.Builder(width, height).addBackground(new GradiatedBackgroundProducer())
                .addText(textProducer, new DefaultWordRenderer()).addNoise(new CurvedLineNoiseProducer())
                .build();
    }

    public void checkCaptcha(String captchaCode, String captchaSecret) {
        Captcha captcha = captchaRepository.getReferenceById(UUID.fromString(captchaSecret));
        if (captcha.getExpirationTime().isBefore(ZonedDateTime.now())) {
            throw new AuthException("wrong captcha");
        }
    }
}
