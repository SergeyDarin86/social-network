package ru.skillbox.diplom.group40.social.network.impl.utils.aspects.anotation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;

import java.util.*;
import java.util.function.Supplier;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CreaterError {
    @Value("${createrError}")
    private boolean createrError;
    private  final AccountService accountService;
    private final FriendService friendService;
    private List<Supplier> supplierList;

    @Scheduled(fixedRate = 2000)
    public void createrErrorPerSecond(){
        if(!createrError){
            return;
        }
        Random random = new Random();
        supplierList = new ArrayList<>();
        supplierList.add(()->friendService.getAllFriendsById(UUID.randomUUID()));
        supplierList.add(()->friendService.getById(UUID.randomUUID()));
        supplierList.add(()->friendService.getFriendsStatus(Collections.singletonList(UUID.randomUUID())));
        supplierList.add(()->accountService.getId(UUID.randomUUID()));
        int num = random.nextInt(supplierList.size());
        supplierList.get(num).get();
    }
}
