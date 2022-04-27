package com.nixsolutions.clouds.vkazakov.aws.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoMapper {

    public String map(MultipartFile multipartFile) {
    if(multipartFile == null){return "";}
        return   multipartFile.getOriginalFilename();
    }

public MultipartFile map(String photoLink) {
        //get from s3
        return null;
}

}
