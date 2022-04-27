package com.nixsolutions.clouds.vkazakov.aws.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
