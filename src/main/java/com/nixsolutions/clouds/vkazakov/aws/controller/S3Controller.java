package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.service.S3BucketManager;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/s3")
public class S3Controller {
    @Autowired
    private S3BucketManager s3BucketManager;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        s3BucketManager.uploadFile(file);
    }

    @PostMapping("/delete")
    public void deleteFile() {
        s3BucketManager.deleteFile("mynew-aws-test-bucket", "er2.jpg");
    }
}
