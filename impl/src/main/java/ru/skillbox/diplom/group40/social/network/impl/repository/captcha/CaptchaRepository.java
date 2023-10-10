package ru.skillbox.diplom.group40.social.network.impl.repository.captcha;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.captcha.Captcha;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

@Repository
public interface CaptchaRepository extends BaseRepository<Captcha> {
}
