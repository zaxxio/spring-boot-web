package org.wsd.app.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.config.ResilienceConfig;
import org.wsd.app.exception.UsernameAlreadyExistsException;
import org.wsd.app.security.auth.AuthenticationService;
import org.wsd.app.security.auth.resquest.SignInRequest;
import org.wsd.app.security.auth.resquest.SignUpRequest;
import org.wsd.app.security.auth.impl.TwoFactorFailedException;

import java.util.Collections;

@Log
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(description = "Sign In", summary = "Endpoint for user sign in.")
    @PostMapping(path = "/signIn",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @RateLimiter(name = ResilienceConfig.AUTH_SERVICE_API)
    public ResponseEntity<?> signIn(@Valid @RequestBody
                                    @Parameter(description = "SignInRequest model with username, password and two-factor authentication code to get access token.") SignInRequest signInRequest
    ) throws TwoFactorFailedException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.signIn(signInRequest));
    }
    @Operation(description = "Sign Up", summary = "Endpoint for user sign up.")
    @PostMapping(path = "/signUp",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @RateLimiter(name = ResilienceConfig.AUTH_SERVICE_API)
    public ResponseEntity<?> signUp(@Valid @RequestBody
                                        @Parameter(description = "SignUpRequest model with username, password to register user.") SignUpRequest signUpRequest) throws UsernameAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.signUp(signUpRequest));
    }
}
