package com.nixsolutions.clouds.vkazakov.aws.sqs;

import com.google.gson.Gson;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDtoTest;
import lombok.extern.log4j.Log4j;

import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class SqsReceiver {
    @SqsListener(value = { "https://sqs.eu-central-1.amazonaws.com/463050938679/MyStandartQueue" },
        deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String json) {

        UserDto userDto = new Gson().fromJson(json, UserDto.class);
        log.info("Cool!!!");
        log.info("Message SQS: " + userDto.getEmail());

    }
}
