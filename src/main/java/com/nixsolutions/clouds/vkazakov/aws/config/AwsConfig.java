package com.nixsolutions.clouds.vkazakov.aws.config;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@RequiredArgsConstructor
@Profile("!local")
public class AwsConfig {
    private final AwsConstants awsConstants;

    @Bean
    public AmazonS3 createS3() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(awsConstants.getRegion()).build();
    }

    @Bean
    public AWSCognitoIdentityProvider createCognitoClient() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withRegion(awsConstants.getRegion())
            .build();
    }

//    @Bean
//    public SnsClient createSnsClient() {
//        return SnsClient.builder()
//            .region(Region.of(awsConstants.getRegion()))
//            .build();
//    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(AmazonSQSAsyncClientBuilder.standard()
            .withRegion(awsConstants.getRegion())
            .build());
    }
}
