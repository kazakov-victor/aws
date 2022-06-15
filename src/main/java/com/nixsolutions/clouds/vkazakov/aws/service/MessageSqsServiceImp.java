package com.nixsolutions.clouds.vkazakov.aws.service;

import com.nixsolutions.clouds.vkazakov.aws.entity.MessageSqs;
import com.nixsolutions.clouds.vkazakov.aws.repository.MessageSqsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageSqsServiceImp implements MessageSqsService {
   private final MessageSqsRepository repository;

   @Override
   public void save(MessageSqs messageSqs) {
      repository.save(messageSqs);
   }

   @Override
   public List<MessageSqs> getAllMessages() {
      return repository.findAll();
   }

}
