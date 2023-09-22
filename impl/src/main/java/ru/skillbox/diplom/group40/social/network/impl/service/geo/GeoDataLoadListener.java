package ru.skillbox.diplom.group40.social.network.impl.service.geo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class GeoDataLoadListener {
    private final GeoService geoService;

    @EventListener(ContextRefreshedEvent.class)
    public void loadGeoDataOnApplicationStart(ContextRefreshedEvent event){
        if(geoService.isDataEmpty()){
            log.info("Загрузка городов и стран...");
            geoService.load();
            log.info("Загрузка городов и стран завершена.");
        }
    }
}
