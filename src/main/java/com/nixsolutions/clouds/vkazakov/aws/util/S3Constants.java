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
@ConfigurationProperties(prefix = "s3", ignoreUnknownFields = false)
public class S3Constants {
    private String bucketName;
    private String photoFolder;

}
