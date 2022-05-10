package com.nixsolutions.clouds.vkazakov.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {
    private final AwsConstants awsConstants;
    private final Environment environment;

    @Bean
    public AmazonS3 createS3() {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(getCredentialsProvider())
            .withRegion(awsConstants.getRegion()).build();
    }

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(
            env -> (env.equalsIgnoreCase("local")))) {
            return new AWSStaticCredentialsProvider
                (new BasicAWSCredentials(
                    awsConstants.getAccessKeyId(),
                    awsConstants.getSecretKey()));
        }
        return new AWSStaticCredentialsProvider
            (new BasicAWSCredentials("", ""));
    }

    @Bean
    public AWSCognitoIdentityProvider createCognitoClient() {
        AWSCredentials cred =
            new BasicAWSCredentials(awsConstants.getAccessKeyId(), awsConstants.getSecretKey());
        AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withCredentials(credProvider)
            .withRegion(awsConstants.getRegion())
            .build();
    }
}
