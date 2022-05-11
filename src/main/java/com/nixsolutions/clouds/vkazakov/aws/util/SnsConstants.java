package com.nixsolutions.clouds.vkazakov.aws.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to s3 client.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@Data
@Component
@ConfigurationProperties(prefix = "sns", ignoreUnknownFields = false)
public class SnsConstants {
    private String topicArn;
    private String region;
}
