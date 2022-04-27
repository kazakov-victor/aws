package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.amazonaws.services.cognitoidp.model.AdminListUserAuthEventsResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.nixsolutions.clouds.vkazakov.aws.dto.LoginDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserPasswordUpdateDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserSignUpDTO;
import com.nixsolutions.clouds.vkazakov.aws.response.AuthenticatedResponse;
import com.nixsolutions.clouds.vkazakov.aws.response.BaseResponse;
import com.nixsolutions.clouds.vkazakov.aws.service.S3BucketManager;
import com.nixsolutions.clouds.vkazakov.aws.service.UserCognitoService;
import java.io.IOException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@Validated
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private S3BucketManager s3BucketManager;
    private final UserCognitoService userCognitoService;

    public AuthController(UserCognitoService userCognitoService) {
        this.userCognitoService = userCognitoService;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<BaseResponse> signUp(@ModelAttribute @Validated UserSignUpDTO signUpDTO) {
        UserType result = userCognitoService.createUser(signUpDTO);
        try {
            s3BucketManager.uploadFile(signUpDTO.getPhoto());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new BaseResponse(result,
            "User account created successfully", false), HttpStatus.CREATED);
    }

    @PostMapping("/username")
    public ResponseEntity<BaseResponse> login(@RequestBody @Validated LoginDTO loginRequest) {
        return new ResponseEntity<>(userCognitoService.authenticate(loginRequest), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<BaseResponse> changePassword(@RequestBody @Validated
                                                           UserPasswordUpdateDTO userPasswordUpdateDTO) {
        AuthenticatedResponse authenticatedResponse =
            userCognitoService.updateUserPassword(userPasswordUpdateDTO);

        return new ResponseEntity<>(
            new BaseResponse(authenticatedResponse, "Update successfully", false), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.contains("Bearer ")) {
            String accessToken = bearerToken.replace("Bearer ", "");

            userCognitoService.logout(accessToken);

            return new ResponseEntity<>(new BaseResponse(null, "Logout successfully", false),
                HttpStatus.OK);
        }
        return new ResponseEntity<>(new BaseResponse(null, "Header not correct"),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/forget-password")
    public ResponseEntity<BaseResponse> forgotPassword(
        @NotNull @NotEmpty @Email @RequestParam("email") String email) {
        ForgotPasswordResult result = userCognitoService.userForgotPassword(email);
        return new ResponseEntity<>(new BaseResponse(
            result.getCodeDeliveryDetails().getDestination(),
            "You should soon receive an email which will allow you to reset your password." +
                " Check your spam and trash if you can't find the email.",
            false), HttpStatus.OK);
    }

    @GetMapping(value = "/user-events")
    public ResponseEntity<BaseResponse> getUserAuthEvents(
        @RequestParam(value = "username") String username,
        @RequestParam(value = "maxResult", defaultValue = "0") int maxResult,
        @RequestParam(value = "nextToken", required = false) String nextToken) {

        AdminListUserAuthEventsResult result =
            userCognitoService.userAuthEvents(username, maxResult, nextToken);
        return new ResponseEntity<>(new BaseResponse(
            result, "user data", false), HttpStatus.OK);

    }

}
