package com.nixsolutions.clouds.vkazakov.aws.mapper;

import com.nixsolutions.clouds.vkazakov.aws.service.S3BucketService;
import java.io.IOException;
import lombok.extern.log4j.Log4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Log4j
public class PhotoMapper {
    private final S3BucketService s3;

    public PhotoMapper(S3BucketService s3) {
        this.s3 = s3;
    }

    public String map(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return "";
        }
        return multipartFile.getOriginalFilename();
    }

    public MultipartFile map(String photoLink) {
        try {
            return new MockMultipartFile(photoLink, s3.downloadFile(photoLink));
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
