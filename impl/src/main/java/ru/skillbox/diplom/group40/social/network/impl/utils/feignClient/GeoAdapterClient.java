package ru.skillbox.diplom.group40.social.network.impl.utils.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

//@FeignClient(value = "Adapter",url="http://localhost:8082/msg")
@FeignClient(value = "Adapter", url="${feign.client.url}")
public interface GeoAdapterClient {
    @PutMapping ("/load")
    void load();
}
