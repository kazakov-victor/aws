package com.nixsolutions.clouds.vkazakov.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {
    private final AwsConstants awsConstants;

    @Bean
    public AmazonS3 createS3() {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider
                (new BasicAWSCredentials(
                    awsConstants.getAccessKeyId(),
                    awsConstants.getSecretKey())))
            .withRegion(Regions.EU_CENTRAL_1).build();
    }

    @Bean
    public AmazonS3 createS3Inner() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(Regions.EU_CENTRAL_1).build();
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
