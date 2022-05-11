package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.nixsolutions.clouds.vkazakov.aws.util.S3Constants;
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
    private final S3Constants s3Constants;
    private final AmazonS3 amazonS3;

    public void uploadFile(MultipartFile content) {
        try {
            Path path = Paths.get(Objects.requireNonNull(content.getOriginalFilename()));
            content.transferTo(path);
            amazonS3.putObject(s3Constants.getBucketName(), content.getOriginalFilename(), path.toFile());
        } catch (AmazonServiceException | IOException exception) {
            log.error(exception.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(s3Constants.getBucketName(), fileName));
        } catch (SdkClientException exception) {
            log.error(exception.getMessage());
        }
    }
}

