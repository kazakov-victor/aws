package com.nixsolutions.clouds.vkazakov.aws.dto;

import com.nixsolutions.clouds.vkazakov.aws.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Size(min = 2, max = 100,
            message = "Please insert first name from 2 till 100 letters!")
    @NotNull(message = "Please insert first name from 2 till 100 letters!")
    @NotEmpty(message = "Please insert first name from 2 till 100 letters!")
    private String firstName;

    @Size(min = 2, max = 100,
            message = "Please insert last name from 2 till 100 letters!")
    @NotNull(message = "Please insert last name from 2 till 100 letters!")
    @NotEmpty(message = "Please insert last name from 2 till 100 letters!")
    private String lastName;

    @NotNull(message = "Please insert username!")
    @NotEmpty(message = "Please insert username!")
    private String username;

    @NotEmpty(message = "Please insert email!")
    @Pattern(regexp = Constants.EMAIL_REGEX,
            message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Please insert password!")
    @NotEmpty(message = "Please insert password!")
    private String password;

    @NotNull(message = "Please insert date in format 2000-01-01!")
    @Pattern(regexp = Constants.DATE_REGEX,
            message = "Please insert date in format 2000-01-01!")
    private String birthdate;

    @NotNull(message = "Incorrect role!")
    private String roleId;

    private String phoneNumber;

    private MultipartFile photo;

}



