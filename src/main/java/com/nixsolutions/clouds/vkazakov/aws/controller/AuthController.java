package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.nixsolutions.clouds.vkazakov.aws.dto.LoginDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.PasswordUpdateDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.AuthenticatedResponse;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.BaseResponse;
import com.nixsolutions.clouds.vkazakov.aws.service.CognitoUserService;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/auth")
public class AuthController {
    private final CognitoUserService cognitoUserService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<BaseResponse> signUp(@ModelAttribute @Validated UserDto userDto) {
        return new ResponseEntity<>(new BaseResponse(cognitoUserService.signUp(userDto),
            "User account created successfully", false), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse> login(@RequestBody @Validated LoginDTO loginRequest) {
        return new ResponseEntity<>(cognitoUserService.authenticate(loginRequest), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<BaseResponse> changePassword(@RequestBody @Validated
                                                           PasswordUpdateDTO passwordUpdateDTO) {
        AuthenticatedResponse authenticatedResponse =
            cognitoUserService.updateUserPassword(passwordUpdateDTO);

        return new ResponseEntity<>(
            new BaseResponse(authenticatedResponse, "Update successfully", false), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.contains("Bearer ")) {
            String accessToken = bearerToken.replace("Bearer ", "");

            cognitoUserService.signOut(accessToken);

            return new ResponseEntity<>(new BaseResponse(null, "Logout successfully", false),
                HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResponse(null, "Header not correct"),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/forget-password")
    public ResponseEntity<BaseResponse> forgotPassword(
        @NotNull @NotEmpty @Email @RequestParam("email") String email) {
        ForgotPasswordResult result = cognitoUserService.forgotPassword(email);
        return new ResponseEntity<>(new BaseResponse(
            result.getCodeDeliveryDetails().getDestination(),
            "You should soon receive an email which will allow you to reset your password." +
                " Check your spam and trash if you can't find the email.",
            false), HttpStatus.OK);
    }

}
