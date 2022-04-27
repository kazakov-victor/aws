package com.nixsolutions.clouds.vkazakov.aws.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminListUserAuthEventsRequest;
import com.amazonaws.services.cognitoidp.model.AdminListUserAuthEventsResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.InternalErrorException;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserPoolAddOnNotEnabledException;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.nixsolutions.clouds.vkazakov.aws.controller.AdminRestController;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.mapper.UserSignUpMapper;
import com.nixsolutions.clouds.vkazakov.aws.util.CognitoAttributesEnum;
import com.nixsolutions.clouds.vkazakov.aws.config.AwsConfig;
import com.nixsolutions.clouds.vkazakov.aws.dto.UserSignUpDTO;
import com.nixsolutions.clouds.vkazakov.aws.exception.FailedAuthenticationException;
import com.nixsolutions.clouds.vkazakov.aws.exception.InvalidParameterException;
import com.nixsolutions.clouds.vkazakov.aws.exception.InvalidPasswordException;
import com.nixsolutions.clouds.vkazakov.aws.exception.ServiceException;
import com.nixsolutions.clouds.vkazakov.aws.exception.UserNotFoundException;
import com.nixsolutions.clouds.vkazakov.aws.exception.UsernameExistsException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.amazonaws.services.cognitoidp.model.ChallengeNameType.NEW_PASSWORD_REQUIRED;

@RequiredArgsConstructor
@Slf4j
@Service
public class CognitoUserServiceImpl implements CognitoUserService {

    @Autowired
    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    @Autowired
    private final AwsConfig awsConfig;

    @Autowired
    private final AdminRestController adminRestController;
    @Autowired
    private final UserSignUpMapper userSignUpMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserType signUp(UserSignUpDTO signUpDTO) {
        UserDto userDto = userSignUpMapper.toDto(signUpDTO);
        adminRestController.saveUser(userDto);
        try {
            final AdminCreateUserRequest signUpRequest = new AdminCreateUserRequest()
                .withUserPoolId(awsConfig.getUserPoolId())
                .withTemporaryPassword(generateValidPassword())
                // Specify "EMAIL" if email will be used to send the welcome message
                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                .withUsername(signUpDTO.getUsername())
                .withMessageAction(MessageActionType.SUPPRESS)
                .withUserAttributes(
                    new AttributeType().withName("birthdate").withValue(signUpDTO.getBirthdate()),
                    new AttributeType().withName("custom:firstName").withValue(signUpDTO.getFirstName()),
                    new AttributeType().withName("custom:lastName").withValue(signUpDTO.getLastName()),
                    new AttributeType().withName("email").withValue(signUpDTO.getEmail()),
                    new AttributeType().withName("email_verified").withValue("true"),
                    new AttributeType().withName("phone_number")
                        .withValue(signUpDTO.getPhoneNumber()),
                    new AttributeType().withName("phone_number_verified").withValue("true"));

            // create user
            AdminCreateUserResult createUserResult =
                awsCognitoIdentityProvider.adminCreateUser(signUpRequest);
            log.info("Created User id: {}", createUserResult.getUser().getUsername());

            // assign the roles
//            signUpDTO.getRoles().forEach(r -> addUserToGroup(signUpDTO.getEmail(), r));
            addUserToGroup(signUpDTO.getUsername(), "ROLE_USER");

            // set permanent password
            setUserPassword(signUpDTO.getUsername(), signUpDTO.getPassword());

            return createUserResult.getUser();

        } catch (com.amazonaws.services.cognitoidp.model.UsernameExistsException e) {
            throw new UsernameExistsException(
                "User name that already exists");
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new InvalidPasswordException(
                "Invalid password.", e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserToGroup(String username, String groupName) {

        try {
            // add user to group
            AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                .withGroupName(groupName)
                .withUserPoolId(awsConfig.getUserPoolId())
                .withUsername(username);

            awsCognitoIdentityProvider.adminAddUserToGroup(addUserToGroupRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(
                String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminSetUserPasswordResult setUserPassword(String username, String password) {

        try {
            // Sets the specified user's password in a user pool as an administrator. Works on any user.
            AdminSetUserPasswordRequest adminSetUserPasswordRequest =
                new AdminSetUserPasswordRequest()
                    .withUsername(username)
                    .withPassword(password)
                    .withUserPoolId(awsConfig.getUserPoolId())
                    .withPermanent(true);

            return awsCognitoIdentityProvider.adminSetUserPassword(adminSetUserPasswordRequest);
        } catch (com.amazonaws.services.cognitoidp.model.InvalidPasswordException e) {
            throw new FailedAuthenticationException(
                String.format("Invalid parameter: %s", e.getErrorMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdminInitiateAuthResult> initiateAuth(String username, String password) {

        final Map<String, String> authParams = new HashMap<>();
        authParams.put(CognitoAttributesEnum.USERNAME.name(), username);
        authParams.put(CognitoAttributesEnum.PASSWORD.name(), password);
        authParams.put(CognitoAttributesEnum.SECRET_HASH.name(),
            calculateSecretHash(awsConfig.getAppClientId(), awsConfig.getAppClientSecret(),
                username));

        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
            .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
            .withClientId(awsConfig.getAppClientId())
            .withUserPoolId(awsConfig.getUserPoolId())
            .withAuthParameters(authParams);

        return adminInitiateAuthResult(authRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AdminRespondToAuthChallengeResult> respondToAuthChallenge(
        String username, String newPassword, String session) {
        AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest();
        request.withChallengeName(NEW_PASSWORD_REQUIRED)
            .withUserPoolId(awsConfig.getUserPoolId())
            .withClientId(awsConfig.getAppClientId())
            .withSession(session)
            .addChallengeResponsesEntry("userAttributes.name", "aek")
            .addChallengeResponsesEntry(CognitoAttributesEnum.USERNAME.name(), username)
            .addChallengeResponsesEntry(CognitoAttributesEnum.NEW_PASSWORD.name(), newPassword)
            .addChallengeResponsesEntry(CognitoAttributesEnum.SECRET_HASH.name(),
                calculateSecretHash(awsConfig.getAppClientId(), awsConfig.getAppClientSecret(),
                    username));

        try {
            return Optional.of(awsCognitoIdentityProvider.adminRespondToAuthChallenge(request));
        } catch (NotAuthorizedException e) {
            throw new NotAuthorizedException("User not found." + e.getErrorMessage());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User not found.", e);
        } catch (InvalidPasswordException e) {
            throw new InvalidPasswordException("Invalid password.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdminListUserAuthEventsResult getUserAuthEvents(String username, int maxResult,
                                                           String nextToken) {
        try {
            AdminListUserAuthEventsRequest userAuthEventsRequest =
                new AdminListUserAuthEventsRequest();
            userAuthEventsRequest.setUsername(username);
            userAuthEventsRequest.setUserPoolId(awsConfig.getUserPoolId());
            userAuthEventsRequest.setMaxResults(maxResult);
            if (Strings.isNotBlank(nextToken)) {
                userAuthEventsRequest.setNextToken(nextToken);
            }

            return awsCognitoIdentityProvider.adminListUserAuthEvents(userAuthEventsRequest);
        } catch (InternalErrorException e) {
            throw new InternalErrorException(e.getErrorMessage());
        } catch (com.amazonaws.services.cognitoidp.model.InvalidParameterException | UserPoolAddOnNotEnabledException e) {
            throw new InvalidParameterException(
                String.format("Amazon Cognito service encounters an invalid parameter %s",
                    e.getErrorMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSignOutResult signOut(String accessToken) {
        try {
            return awsCognitoIdentityProvider
                .globalSignOut(new GlobalSignOutRequest().withAccessToken(accessToken));
        } catch (NotAuthorizedException e) {
            throw new FailedAuthenticationException(
                String.format("Logout failed: %s", e.getErrorMessage()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ForgotPasswordResult forgotPassword(String username) {
        try {
            ForgotPasswordRequest request = new ForgotPasswordRequest();
            request.withClientId(awsConfig.getAppClientId())
                .withUsername(username)
                .withSecretHash(
                    calculateSecretHash(awsConfig.getAppClientId(), awsConfig.getAppClientSecret(),
                        username));

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
                request.getAuthParameters().get(CognitoAttributesEnum.USERNAME.name());
            throw new UserNotFoundException(String.format("Username %s  not found.", username), e);
        }
    }

    private String calculateSecretHash(String userPoolClientId, String userPoolClientSecret,
                                       String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

        SecretKeySpec signingKey = new SecretKeySpec(
            userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
            HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new ServiceException("Error while calculating ");
        }
    }

    /**
     * @return password generated
     */
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

        return gen.generatePassword(10, splCharRule, lowerCaseRule,
            upperCaseRule, digitRule);
    }

}
