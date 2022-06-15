package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.entity.MessageSqs;
import java.util.List;

public interface MessageSqsService {
    void save(MessageSqs messageSqs);

    List<MessageSqs> getAllMessages();
}
