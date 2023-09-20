package ru.skillbox.diplom.group40.social.network.impl.config.captcha;

import cn.apiclub.captcha.text.producer.TextProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class CustomTextProducer implements TextProducer {
    @Value("${security.captcha.length}")
    private int length;
    private final char[] srcChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final Random RAND = new SecureRandom();

    @Override
    public String getText() {
        StringBuilder capText = new StringBuilder();

        for(int i = 0; i < this.length; i++) {
            capText.append(this.srcChars[RAND.nextInt(this.srcChars.length)]);
        }
        if(capText.length()>2){
            capText.setCharAt(0,capText.charAt(2));
        }
        return capText.toString();
    }
}