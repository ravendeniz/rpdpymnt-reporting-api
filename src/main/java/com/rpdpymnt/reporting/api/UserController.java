package com.rpdpymnt.reporting.api;

import com.rpdpymnt.reporting.dto.LoginRequest;
import com.rpdpymnt.reporting.dto.LoginResponse;
import com.rpdpymnt.reporting.model.UserProfile;
import com.rpdpymnt.reporting.service.TokenService;
import com.rpdpymnt.reporting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import static com.rpdpymnt.reporting.api.BaseController.BASE_PATH;
import static com.rpdpymnt.reporting.api.BaseController.USERS;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Api(tags = USERS, description = "User service")
@RequestMapping(path = BASE_PATH)
@Controller
@Validated
@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveParameterList"})
public class UserController extends BaseController {

    private final UserService userProfileService;
    private final TokenService tokenService;

    private static final String SPECIFIED_EMAIL_DOES_NOT_EXIST = "A user with the specified email does not exist.";
    private static final String USER_NOT_FOUND = "User not found.";

    public UserController(ModelMapper mapper, UserService userProfileService, TokenService tokenService) {
        super(mapper);
        this.userProfileService = userProfileService;
        this.tokenService = tokenService;
    }

    @ApiOperation(value = "Login user (ALL USERS)", tags = {CORE, USERS})
    @ApiResponses({@ApiResponse(code = SC_OK, message = SUCCESSFUL_OPERATION),
            @ApiResponse(code = SC_BAD_REQUEST, message = INVALID_PARAMETERS),
            @ApiResponse(code = SC_UNAUTHORIZED, message = UNAUTHORISED),
            @ApiResponse(code = SC_NOT_FOUND, message = USER_NOT_FOUND)})
    @PostMapping(value = "/users/login", produces = JSON)
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        if (!userProfileService.isUserExist(loginRequest.getEmail())) {
            throw new EntityNotFoundException(SPECIFIED_EMAIL_DOES_NOT_EXIST);
        }
        final UserProfile userProfile = userProfileService.getUserProfileByEmail(loginRequest.getEmail());
        final String token = tokenService.generateToken(loginRequest.getEmail(), loginRequest.getPassword());
        final LoginResponse response = LoginResponse.builder()
                .email(loginRequest.getEmail())
                .accessToken(token)
                .userName(userProfile.getName())
                .roles(userProfile.getRoles())
                .build();
        return ResponseEntity.ok(response);
    }
}
