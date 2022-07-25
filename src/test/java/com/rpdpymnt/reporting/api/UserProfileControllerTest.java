package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.LoginRequest;
import com.rpdpymnt.reporting.dto.LoginResponse;
import com.rpdpymnt.reporting.model.UserProfile;
import com.rpdpymnt.reporting.service.TokenService;
import com.rpdpymnt.reporting.service.UserService;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileControllerTest {

    private static final String EMAIL = "test@test.com";
    private static final String NAME = "name lastname";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    private static final String TOKEN_TYPE = "Bearer";

    private static final String EMAIL_NOT_EXIST = "A user with the specified email does not exist.";
    private static final String WRONG_PASSWORD = "Wrong password.";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Mock
    private UserService userProfileService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserController controller;

    @Test
    public void givenLoginRequestWhenLoginUserThenResultIsOk() {
        // Arrange
        final LoginRequest request = new LoginRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        when(userProfileService.isUserExist(EMAIL)).thenReturn(true);
        when(userProfileService.getUserProfileByEmail(EMAIL)).thenReturn(getUserProfile());
        when(tokenService.generateToken(EMAIL, PASSWORD)).thenReturn(TOKEN);

        // Act
        ResponseEntity<LoginResponse> result = controller.loginUser(request);

        // Assert
        softly.assertThat(result.getBody().getAccessToken()).isEqualTo(TOKEN);
        softly.assertThat(result.getBody().getTokenType()).isEqualTo(TOKEN_TYPE);
        softly.assertThat(result.getStatusCodeValue()).isEqualTo(OK.value());
    }

    @Test
    public void givenLoginRequestWithWrongPasswordWhenLoginUserThenThrowException() {
        // Arrange
        final LoginRequest request = new LoginRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        when(tokenService.generateToken(EMAIL, PASSWORD))
                .thenThrow(new BadCredentialsException(WRONG_PASSWORD));
        when(userProfileService.isUserExist(EMAIL)).thenReturn(true);
        // Assert
        thrown.expect(AuthenticationException.class);
        thrown.expectMessage(WRONG_PASSWORD);
        // Act
        controller.loginUser(request);
    }

    @Test
    public void givenLoginRequestWithNonExistEmailWhenLoginUserThenThrowException() {
        // Arrange
        final LoginRequest request = new LoginRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        when(userProfileService.isUserExist(EMAIL)).thenReturn(false);
        // Assert
        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage(EMAIL_NOT_EXIST);
        // Act
        controller.loginUser(request);
    }

    private UserProfile getUserProfile() {
        final UserProfile userProfile = new UserProfile();
        userProfile.setEmail(EMAIL);
        userProfile.setName(NAME);
        userProfile.setPassword(PASSWORD);
        return userProfile;
    }
}
