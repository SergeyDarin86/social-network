package ru.skillbox.diplom.group40.social.network.impl.utils.auth;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.auth.CaptchaDto;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class CaptchaUtil {
    Captcha captcha;

    public CaptchaDto getCaptcha() {
        captcha = generateCaptcha(200, 60);
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
        return captchaDto;
    }

    public static Captcha generateCaptcha(Integer width, Integer height) {
        return new Captcha.Builder(width, height).addBackground(new GradiatedBackgroundProducer())
                .addText(new DefaultTextProducer(), new DefaultWordRenderer()).addNoise(new CurvedLineNoiseProducer())
                .build();
    }

    public boolean captchaIsCorrect(String captchaCode) {
        return captchaCode.equals(captcha.getAnswer());
    }
}
