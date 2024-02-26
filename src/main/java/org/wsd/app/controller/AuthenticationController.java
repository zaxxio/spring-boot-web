package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.messaging.pubs.ProducerService;
import org.wsd.app.payload.Payload;
import org.wsd.app.security.auth.AuthenticationService;
import org.wsd.app.security.auth.response.SignInResponse;
import org.wsd.app.security.auth.response.SignUpResponse;
import org.wsd.app.security.auth.resquest.SignInRequest;
import org.wsd.app.security.auth.resquest.SignUpRequest;
import org.wsd.app.security.auth.impl.TwoFactorFailedException;

import java.io.IOException;

@Log
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public final ProducerService producerService;

    @Operation(description = "Sign In", summary = "Endpoint for user sign in.")
    @PostMapping(path = "/signIn",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signIn(@Valid @RequestBody @Parameter(description = "sdfsd") SignInRequest signInRequest) throws TwoFactorFailedException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.signIn(signInRequest));
    }

    @Operation(description = "Sign Up", summary = "Endpoint for user sign up.")
    @PostMapping(path = "/signUp",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(authenticationService.signUp(signUpRequest));
    }

}
