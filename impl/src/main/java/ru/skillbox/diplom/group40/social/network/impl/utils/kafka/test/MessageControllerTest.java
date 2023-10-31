package ru.skillbox.diplom.group40.social.network.impl.utils.kafka.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;

@RestController
@RequestMapping("api/v1/notifications/kafka")
public class MessageControllerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaService kafkaService;

    public MessageControllerTest(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public void published(){
        kafkaTemplate.send("notifications", "test kafka GET");
    }

    @PostMapping
    public void publishes(@RequestBody String message){
        kafkaTemplate.send("notifications", message);
    }

    @GetMapping("/offset")
    public void setOffset(){kafkaService.setOffset();}

}
