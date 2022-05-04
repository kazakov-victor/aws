package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.nixsolutions.clouds.vkazakov.aws.config.AwsConfig;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Log4j
@Component
@RequiredArgsConstructor
public class S3BucketService {
    private final AwsConstants awsConstants;
    private final AwsConfig config;

    public void uploadFile(MultipartFile content) {
        try {
            Path path = Paths.get(Objects.requireNonNull(content.getOriginalFilename()));
            content.transferTo(path);
            AmazonS3 s3 = config.createS3();
            s3.putObject(awsConstants.getBucketName(), content.getOriginalFilename(), path.toFile());
        } catch (AmazonServiceException | IOException exception) {
            log.error(exception.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            AmazonS3 s3 = config.createS3Inner();
            s3.deleteObject(new DeleteObjectRequest(awsConstants.getBucketName(), fileName));
        } catch (SdkClientException exception) {
            log.error(exception.getMessage());
        }
    }

    public S3ObjectInputStream downloadFile(final String fileName) {
        AmazonS3 s3 = config.createS3();
        S3Object object = s3.getObject(awsConstants.getBucketName(), fileName);
        return object.getObjectContent();
    }
}

