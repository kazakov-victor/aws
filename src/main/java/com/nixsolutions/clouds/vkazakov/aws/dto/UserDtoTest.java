package com.nixsolutions.clouds.vkazakov.aws.dto;

import com.nixsolutions.clouds.vkazakov.aws.util.Constants;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoTest {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;


    private String birthdate;


    private String roleId;

    private String phoneNumber;

    private String photo;

}



