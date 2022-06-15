package com.nixsolutions.clouds.vkazakov.aws.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordUpdateDTO extends AuthenticatedChallengeDTO {

    @NonNull
    @NotBlank(message = "New password is mandatory")
    private String password;

    @NonNull
    @NotBlank(message = "Confirm Password is mandatory")
    private String passwordConfirm;
}
