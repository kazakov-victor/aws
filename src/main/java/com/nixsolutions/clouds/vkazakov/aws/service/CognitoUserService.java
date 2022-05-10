package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.nixsolutions.clouds.vkazakov.aws.dto.LoginDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.PasswordUpdateDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.AuthenticatedResponse;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.BaseResponse;

public interface CognitoUserService {
    /**
     * @param username    username
     * @param newPassword new user password
     * @param session     user session di
     * @return AdminRespondToAuthChallengeResult
     */
    AdminRespondToAuthChallengeResult respondToAuthChallenge(
        String username, String newPassword, String session);

    /**
     * Signs out users from all devices.
     *
     * @param accessToken access token
     */
    void signOut(String accessToken);

    /**
     * Send forgot password flow
     *
     * @param username username
     * @return {@link ForgotPasswordResult}
     */
    ForgotPasswordResult forgotPassword(String username);

    /**
     * Add a group to user
     *
     * @param username  user name
     * @param groupName group name
     */
    void addUserToGroup(String username, String groupName);

    /**
     * set permanent password to make user status as CONFIRMED
     *
     * @param username username
     * @param password user password
     */
    void setUserPassword(String username, String password);

    /**
     * Creates a new user in the specified user pool.
     *
     * @return UserType
     */
    UserType signUp(UserDto userDto);

    /**
     * @param userLogin user username infos
     * @return BaseResponse
     */
    BaseResponse authenticate(LoginDTO userLogin);

    /**
     * @param passwordUpdateDTO user password DTO request
     * @return AuthenticatedResponse
     */
    AuthenticatedResponse updateUserPassword(PasswordUpdateDTO passwordUpdateDTO);
}
