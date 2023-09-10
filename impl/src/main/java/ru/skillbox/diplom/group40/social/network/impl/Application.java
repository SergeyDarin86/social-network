package ru.skillbox.diplom.group40.social.network.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepositoryImpl;

/**
 * Application
 *
 * @author Sergey Darin
 */
@SpringBootApplication
@EntityScan("ru/skillbox/diplom/group40/social/network/domain.*")
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
