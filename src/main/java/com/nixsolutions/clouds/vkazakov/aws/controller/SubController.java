package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.entity.Message;
import com.nixsolutions.clouds.vkazakov.aws.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sns")
public class SubController {
    private final SnsService snsService;

    @PostMapping("/addEmail")
    String addItems(@RequestBody String email) {
        return snsService.subscriptionWithEmail(email);
    }

    @PostMapping("/delSub")
    String delSub(@RequestBody String email) {
        snsService.unsubscriptionWithEmail(email);
        return email + " was successfully deleted!";
    }

    @PostMapping("/addMessage")
    String addMessage(@RequestBody Message message) {
        return snsService.pubTopic(message.getSubject(), message.getText());
    }

    @GetMapping("/getSubs")
    String getSubs() {
        return snsService.getAllSubscriptions();
    }
}
