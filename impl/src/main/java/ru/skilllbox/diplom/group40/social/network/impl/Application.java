package ru.skilllbox.diplom.group40.social.network.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Application
 *
 * @author Sergey Darin
 */
@SpringBootApplication
@EntityScan("ru.skilllbox.diplom.group40.social.network.domain.account")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
