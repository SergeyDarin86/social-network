package ru.skillbox.diplom.group40.social.network.impl.repository.captcha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.captcha.CaptchaEntity;

import java.util.UUID;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaEntity, UUID> {
}
