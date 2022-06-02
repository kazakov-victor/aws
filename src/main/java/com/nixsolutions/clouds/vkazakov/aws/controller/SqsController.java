package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.entity.MessageSqs;
import com.nixsolutions.clouds.vkazakov.aws.service.MessageSqsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sqs")
public class SqsController {
    private final MessageSqsService messageSqsService;

    @GetMapping("/list")
    List<MessageSqs> getAllMessage() {
        return messageSqsService.getAllMessages();

    }
}
