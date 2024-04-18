/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.controller.common;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import org.wsd.app.config.ResilienceConfig;
import org.wsd.app.exception.UsernameAlreadyExistsException;
import org.wsd.app.security.auth.AuthenticationService;
import org.wsd.app.security.auth.resquest.SignInRequest;
import org.wsd.app.security.auth.resquest.SignUpRequest;
import org.wsd.app.security.auth.impl.TwoFactorFailedException;

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
