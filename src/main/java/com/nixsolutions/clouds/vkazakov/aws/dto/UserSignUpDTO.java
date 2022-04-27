package com.nixsolutions.clouds.vkazakov.aws.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserSignUpDTO {
    private String firstName;
    private String lastName;
    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    @Email
    private String email;

    private String password;

    private String phoneNumber;

    private String birthdate;

    private MultipartFile photo;


}




