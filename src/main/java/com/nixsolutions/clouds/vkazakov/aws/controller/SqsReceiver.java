package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.entity.MessageSqs;
import com.nixsolutions.clouds.vkazakov.aws.service.MessageSqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j
public class SqsReceiver {
    private final static String QUEUE_NAME= "MyStandartQueue";
    private final MessageSqsService messageSqsService;

    @SqsListener(value = {QUEUE_NAME}, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String text) {
        messageSqsService.save(new MessageSqs(text));
    }
}
