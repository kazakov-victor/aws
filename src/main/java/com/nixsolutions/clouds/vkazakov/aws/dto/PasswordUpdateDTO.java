package com.nixsolutions.clouds.vkazakov.aws.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PasswordUpdateDTO extends AuthenticatedChallengeDTO {

    @NonNull
    @NotBlank(message = "New password is mandatory")
    private String password;

    @NonNull
    @NotBlank(message = "Confirm Password is mandatory")
    private String passwordConfirm;
}
