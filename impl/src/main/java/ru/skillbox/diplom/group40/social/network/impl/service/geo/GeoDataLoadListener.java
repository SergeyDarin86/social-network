package ru.skillbox.diplom.group40.social.network.impl.service.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.impl.utils.feignClient.GeoAdapterClient;


@Slf4j
@Component
@RequiredArgsConstructor
public class GeoDataLoadListener {
    private final GeoService geoService;
    private final GeoAdapterClient geoAdapterClient;
    @Value("${feign.client.enable}")
    private boolean localhost;

    @EventListener(ContextRefreshedEvent.class)
    public void loadGeoDataOnApplicationStart(ContextRefreshedEvent event) {
        if (geoService.isDataEmpty()) {
            log.info("Загрузка городов и стран...");
            if (localhost) {
                log.info("Загрузка из приложения");
                geoService.testLoadGeo();
            } else {
                log.info("Загрузка из геоадаптера");
                geoAdapterClient.load();
            }
        }
    }
}
