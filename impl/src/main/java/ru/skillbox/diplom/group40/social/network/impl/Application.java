package ru.skillbox.diplom.group40.social.network.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepositoryImpl;

/**
 * Application
 *
 * @author Sergey Darin
 */
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@EnableCaching
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@EntityScan("ru/skillbox/diplom/group40/social/network/domain.*")
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
