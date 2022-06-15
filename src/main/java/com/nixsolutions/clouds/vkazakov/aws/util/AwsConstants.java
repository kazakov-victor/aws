package com.nixsolutions.clouds.vkazakov.aws.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to aws client.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "aws", ignoreUnknownFields = false)
public class AwsConstants {
    private String userPoolId;
    private String identityPoolId;
    private String jwkUrl;
    private String region;
    private String accessKey;
    private String accessKeyId;
    private String secretKey;
    private String appClientId;
    private String appClientSecret;
    private int connectionTimeout = 2000;
    private int readTimeout = 2000;

    public String getJwkUrl() {
        return this.jwkUrl != null && !this.jwkUrl.isEmpty() ? this.jwkUrl : String
            .format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", this.region,
                this.userPoolId);
    }

    public String getUserPoolIdURL() {
        return String
            .format("https://cognito-idp.%s.amazonaws.com/%s", this.region, this.userPoolId);
    }

}
