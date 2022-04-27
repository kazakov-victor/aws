package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.services.cognitoidp.model.AdminListUserAuthEventsResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.UserType;

import com.nixsolutions.clouds.vkazakov.aws.response.AuthenticatedResponse;
import com.nixsolutions.clouds.vkazakov.aws.response.BaseResponse;
import com.nixsolutions.clouds.vkazakov.aws.dto.LoginDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserPasswordUpdateDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserSignUpDTO;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface UserCognitoService {

    /**
     * @param userLogin user username infos
     * @return BaseResponse
     */
    BaseResponse authenticate(LoginDTO userLogin);


    /**
     * @param userPasswordUpdateDTO user password DTO request
     * @return AuthenticatedResponse
     */
    AuthenticatedResponse updateUserPassword(UserPasswordUpdateDTO userPasswordUpdateDTO);


    /**
     * @param accessToken user authenticate access token
     */
    void logout(@NotNull String accessToken);


    /**
     * @param username username
     * @return {@link ForgotPasswordResult}
     */
    ForgotPasswordResult userForgotPassword(String username);

    /**
     * Creates a new user in the specified user pool.
     *
     * @param signUpDTO user info
     * @return UserType
     */
    UserType createUser(UserSignUpDTO signUpDTO);

    /**
     * @param username  username username
     * @param maxResult The maximum number of authentication events to return.
     * @param nextToken A pagination token.
     * @return AdminListUserAuthEventsResult
     */
    AdminListUserAuthEventsResult userAuthEvents(String username, int maxResult, String nextToken);
}
