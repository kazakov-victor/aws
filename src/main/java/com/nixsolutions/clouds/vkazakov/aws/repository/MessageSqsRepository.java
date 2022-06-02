package com.nixsolutions.clouds.vkazakov.aws.repository;

import com.nixsolutions.clouds.vkazakov.aws.entity.MessageSqs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageSqsRepository extends JpaRepository<MessageSqs, Long> {
}

