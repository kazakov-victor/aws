package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.nixsolutions.clouds.vkazakov.aws.config.AwsConfig;
import com.nixsolutions.clouds.vkazakov.aws.config.WebSecurityConfig;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3BucketManager {
    @Autowired
    private AwsConfig awsConfig;
    @Autowired
    private WebSecurityConfig config;

    public void uploadFile(MultipartFile content) throws IOException {
        Path path = Paths.get(content.getOriginalFilename());
        content.transferTo(path);
        try {
            AmazonS3 s3 = config.createS3();
            s3.putObject(awsConfig.getBucketName(), content.getOriginalFilename(), path.toFile());
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }

    public void deleteFile(String bucketName, String fileName) {
        try {
            AmazonS3 s3 = config.createS3();
            s3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}

//    public S3Object downloadFile(final String fileName, final String bucketName) {
//        return amazonS3Client.getObject(bucketName,  fileName);
//    }

//
//    public File downloadImage(final String fileName) {
//
//        try {
//            final S3Object s3Object = downloadFile(fileName, bucketName);
//            final S3ObjectInputStream inputStream = s3Object.getObjectContent();
//
//            byte[] bytes = null;
//            File file = new File(fileName);
//            try {
//                bytes = StreamUtils.copyToByteArray(inputStream);
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(bytes);
//            } catch (IOException e) {
//                /* Handle Exception */
//            }
//            return file;
//        }catch (AmazonS3Exception amazonS3Exception){
//            throw amazonS3Exception;
//        }
//    }

//    public ResponseEntity downloadImageAsResponseEntity(final String fileName) {
//
//        try {
//            final S3Object s3Object = downloadFile(fileName, bucketName);
//            final S3ObjectInputStream inputStream = s3Object.getObjectContent();
//
//            byte[] bytes = null;
//            try {
//                bytes = StreamUtils.copyToByteArray(inputStream);
//            } catch (IOException e) {
//                /* Handle Exception */
//            }
//
//            String contentType = s3Object.getObjectMetadata().getContentType();
//            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(bytes);
//        }catch (AmazonS3Exception amazonS3Exception){
//            throw amazonS3Exception;
//        }
//    }
//}
