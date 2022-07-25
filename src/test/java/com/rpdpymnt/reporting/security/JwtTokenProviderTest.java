package com.rpdpymnt.reporting.security;

import com.rpdpymnt.reporting.RpdpymntProperties;
import com.rpdpymnt.reporting.settings.JwtSettings;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest {

    private static final String SECRET_KEY = "JWTSuperSecretKey";
    private static final long EXPIRE_IN_MS = 604800000L;
    private static final long USER_ID = 1L;
    private static final String PASSWORD = "testpassword";
    private static final String INVALID_TOKEN = "invalidToken";

    @InjectMocks
    private JwtTokenProvider tokenProvider;

    @Mock
    private RpdpymntProperties properties;

    @Mock
    private JwtSettings settings;

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    private Authentication authentication;

    @Before
    public void setUp() {
        final UserPrincipal principal = new UserPrincipal();
        principal.setId(USER_ID);
        authentication = new UsernamePasswordAuthenticationToken(principal, PASSWORD);
    }

    @Test
    public void givenAuthenticationWithUserIdAndPasswordWhenGenerateTokenThenValidateTokenAndCheckUserIdIsOk() {
        // Arrange
        when(properties.getJwt()).thenReturn(settings);
        when(settings.getSecret()).thenReturn(SECRET_KEY);
        when(settings.getExpirationInMs()).thenReturn(EXPIRE_IN_MS);

        // Act
        final String jwt = tokenProvider.generateToken(authentication);
        final boolean isValid = tokenProvider.validateToken(jwt);
        final long idFromToken = tokenProvider.getUserIdFromJwt(jwt);

        // Assert
        softly.assertThat(isValid).isTrue();
        softly.assertThat(idFromToken).isEqualTo(USER_ID);
    }

    @Test
    public void givenInvalidTokenWhenValidateTokenThenThrowException() {
        // Arrange
        when(properties.getJwt()).thenReturn(settings);
        when(settings.getSecret()).thenReturn(SECRET_KEY);

        // Act
        boolean isValid = tokenProvider.validateToken(INVALID_TOKEN);

        // Assert
        softly.assertThat(isValid).isFalse();
    }

}
