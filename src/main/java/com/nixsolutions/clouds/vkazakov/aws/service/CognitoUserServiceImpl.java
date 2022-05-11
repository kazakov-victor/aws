package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.nixsolutions.clouds.vkazakov.aws.dto.AuthenticatedChallengeDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.LoginDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.PasswordUpdateDTO;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.AuthenticatedResponse;
import com.nixsolutions.clouds.vkazakov.aws.dto.response.BaseResponse;
import com.nixsolutions.clouds.vkazakov.aws.exception.FailedAuthenticationException;
import com.nixsolutions.clouds.vkazakov.aws.exception.InvalidPasswordException;
import com.nixsolutions.clouds.vkazakov.aws.exception.ServiceException;
import com.nixsolutions.clouds.vkazakov.aws.exception.UserNotFoundException;
import com.nixsolutions.clouds.vkazakov.aws.exception.UsernameExistsException;
import com.nixsolutions.clouds.vkazakov.aws.util.AwsConstants;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.amazonaws.services.cognitoidp.model.ChallengeNameType.NEW_PASSWORD_REQUIRED;

@Log4j
@Service
@RequiredArgsConstructor
public class CognitoUserServiceImpl implements CognitoUserService {
    private final static String USER_NAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";
    private final static String SECRET_HASH = "SECRET_HASH";
    private final static String NEW_PASSWORD = "NEW_PASSWORD";
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;
    private final AwsConstants awsConstants;
    private final UserService userService;
    private final S3BucketService s3BucketService;

    @Override
    public UserType signUp(UserDto userDto) {
        userService.saveUserDto(userDto);
        try {
            final AdminCreateUserRequest signUpRequest = getSignUpRequest(userDto);
            AdminCreateUserResult createUserResult =
                awsCognitoIdentityProvider.adminCreateUser(signUpRequest);
            s3BucketService.uploadFile(userDto.getPhoto());
            addUserToGroup(userDto.getUsername(), "ROLE_USER");
            setUserPassword(userDto.getUsername(), userDto.getPassword());
            log.info("Created User id: {}" + createUserResult.getUser().getUsername());
            return createUserResult.getUser();

        } catch (com.amazonaws.services.cognitoidp.model.UsernameExistsException e) {
            throw new UsernameExistsException(
                "User name that already exists");
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new InvalidPasswordException(
                "Invalid password.", e);
        }
    }

    private AdminCreateUserRequest getSignUpRequest(UserDto userDto) {
        return new AdminCreateUserRequest()
            .withUserPoolId(awsConstants.getUserPoolId())
            .withTemporaryPassword(generateValidPassword())
            .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
            .withUsername(userDto.getUsername())
            .withMessageAction(MessageActionType.SUPPRESS)
            .withUserAttributes(
                new AttributeType().withName("birthdate").withValue(userDto.getBirthdate()),
                new AttributeType().withName("custom:firstName")
                    .withValue(userDto.getFirstName()),
                new AttributeType().withName("custom:lastName")
                    .withValue(userDto.getLastName()),
                new AttributeType().withName("email").withValue(userDto.getEmail()),
                new AttributeType().withName("email_verified").withValue("true"),
                new AttributeType().withName("phone_number")
                    .withValue(userDto.getPhoneNumber()),
                new AttributeType().withName("phone_number_verified").withValue("true"));
    }

    @Override
    public void addUserToGroup(String username, String groupName) {
        try {
            AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                .withGroupName(groupName)
                .withUserPoolId(awsConstants.getUserPoolId())
                .withUsername(username);

            awsCognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(
                String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }

    @Override
    public void setUserPassword(String username, String password) {
        try {
            AdminSetUserPasswordRequest adminSetUserPasswordRequest =
                new AdminSetUserPasswordRequest()
                    .withUsername(username)
                    .withPassword(password)
                    .withUserPoolId(awsConstants.getUserPoolId())
                    .withPermanent(true);

            awsCognitoIdentityProvider.adminSetUserPassword(adminSetUserPasswordRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(
                String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }

    @Override
    public BaseResponse authenticate(LoginDTO userLogin) {
        AdminInitiateAuthResult result =
            initiateAuth(userLogin.getUsername(), userLogin.getPassword())
                .orElseThrow(
                    () -> new com.amazonaws.services.cognitoidp.model.UserNotFoundException(
                        String.format("Username %s  not found.", userLogin.getUsername())));
        if (ObjectUtils.nullSafeEquals(NEW_PASSWORD_REQUIRED.name(), result.getChallengeName())) {
            return getChangePassword(userLogin, result);
        }
        return getSuccessfulLogin(userLogin, result);
    }

    private BaseResponse getChangePassword(LoginDTO userLogin, AdminInitiateAuthResult authResult) {
        return new BaseResponse(AuthenticatedChallengeDTO.builder()
            .challengeType(NEW_PASSWORD_REQUIRED.name())
            .sessionId(authResult.getSession())
            .username(userLogin.getUsername())
            .build(), "First time login - Password change required", false);
    }

    private BaseResponse getSuccessfulLogin(LoginDTO userLogin,
                                            AdminInitiateAuthResult result) {
        return new BaseResponse(
            getAuthenticatedResponse(result.getAuthenticationResult(), userLogin.getUsername()),
            "Login successful", false);
    }

    private AuthenticatedResponse getAuthenticatedResponse(
        AuthenticationResultType authenticationResult, String username) {
        return AuthenticatedResponse.builder()
            .accessToken(authenticationResult.getAccessToken())
            .idToken(authenticationResult.getIdToken())
            .refreshToken(authenticationResult.getRefreshToken())
            .username(username)
            .build();
    }

    private Optional<AdminInitiateAuthResult> initiateAuth(String username, String password) {

        final Map<String, String> authParams = new HashMap<>();
        authParams.put(USER_NAME, username);
        authParams.put(PASSWORD, password);
        authParams.put(SECRET_HASH, calculateSecretHash(username));

        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
            .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
            .withClientId(awsConstants.getAppClientId())
            .withUserPoolId(awsConstants.getUserPoolId())
            .withAuthParameters(authParams);

        return adminInitiateAuthResult(authRequest);
    }

    @Override
    public AdminRespondToAuthChallengeResult respondToAuthChallenge(
        String username, String newPassword, String session) {
        AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
        request.withChallengeName(NEW_PASSWORD_REQUIRED)
            .withUserPoolId(awsConstants.getUserPoolId())
            .withClientId(awsConstants.getAppClientId())
            .withSession(session)
            .addChallengeResponsesEntry("userAttributes.name", "aek")
            .addChallengeResponsesEntry(USER_NAME, username)
            .addChallengeResponsesEntry(NEW_PASSWORD, newPassword)
            .addChallengeResponsesEntry(SECRET_HASH, calculateSecretHash(username));

        try {
            return (awsCognitoIdentityProvider.adminRespondToAuthChallenge(request));
        } catch (NotAuthorizedException e) {
            throw new NotAuthorizedException("User not found." + e.getErrorMessage());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found.", e);
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException("Invalid password.", e);
        }
    }

    @Override
    public void signOut(String accessToken) {
        try {
            awsCognitoIdentityProvider
                .globalSignOut(new GlobalSignOutRequest().withAccessToken(accessToken));
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(
                String.format("Logout failed: %s", e.getErrorMessage()), e);
        }
    }

    @Override
    public ForgotPasswordResult forgotPassword(String username) {
        try {
            ForgotPasswordRequest request = new ForgotPasswordRequest();
            request.withClientId(awsConstants.getAppClientId())
                .withUsername(username)
                .withSecretHash(calculateSecretHash(username));
            return awsCognitoIdentityProvider.forgotPassword(request);
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(
                String.format("Forgot password failed: %s", e.getErrorMessage()), e);
        }
    }

    private Optional<AdminInitiateAuthResult> adminInitiateAuthResult(
        AdminInitiateAuthRequest request) {
        try {
            return Optional.of(awsCognitoIdentityProvider.adminInitiateAuth(request));
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(
                String.format("Authenticate failed: %s", e.getErrorMessage()), e);
        } catch (UserNotFoundException e) {
            String username =
                request.getAuthParameters().get(USER_NAME);
            throw new UserNotFoundException(String.format("Username %s  not found.", username), e);
        }
    }

    private String calculateSecretHash(String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
        SecretKeySpec signingKey = new SecretKeySpec(
            awsConstants.getAppClientSecret().getBytes(StandardCharsets.UTF_8),
            HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(
                awsConstants.getAppClientId().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new ServiceException("Error while calculating ");
        }
    }

    private String generateValidPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "ERRONEOUS_SPECIAL_CHARS";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
    }

    @Override
    public AuthenticatedResponse updateUserPassword(PasswordUpdateDTO passwordUpdateDTO) {

        AdminRespondToAuthChallengeResult result =
            respondToAuthChallenge(passwordUpdateDTO.getUsername(), passwordUpdateDTO.getPassword(),
                passwordUpdateDTO.getSessionId());

        return getAuthenticatedResponse(result.getAuthenticationResult(),
            passwordUpdateDTO.getUsername());
    }
}
