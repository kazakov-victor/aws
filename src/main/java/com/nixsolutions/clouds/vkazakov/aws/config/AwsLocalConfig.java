package com.nixsolutions.clouds.vkazakov.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@RequiredArgsConstructor
@Profile("local")
public class AwsLocalConfig {
    private final AwsConstants awsConstants;

    @Bean
    public AmazonS3 createS3() {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider
                (new BasicAWSCredentials(
                    awsConstants.getAccessKeyId(),
                    awsConstants.getSecretKey())))
            .withRegion(awsConstants.getRegion()).build();
    }

    @Bean
    public AWSCognitoIdentityProvider createCognitoClient() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                awsConstants.getAccessKeyId(), awsConstants.getSecretKey())))
            .withRegion(awsConstants.getRegion())
            .build();
    }

    @Bean
    public SnsClient createSnsClient() {
        return SnsClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials
                    .create(awsConstants.getAccessKeyId(), awsConstants.getSecretKey())))
            .region(Region.of(awsConstants.getRegion()))
            .build();
    }
}
