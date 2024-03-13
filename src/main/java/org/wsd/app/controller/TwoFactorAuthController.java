package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.payload.Payload;
import org.wsd.app.security.auth.response.TwoFactorResponse;
import org.wsd.app.security.auth.resquest.TwoFactorRequest;
import org.wsd.app.security.auth.impl.TwoFactorService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Two Factor Authentication")
@SecurityRequirement(name = "BEARER_TOKEN")
public class TwoFactorAuthController {
    private final TwoFactorService twoFactorService;
    @Operation(description = "Two Factor Authentication", summary = "Endpoint for two factor authentication service enable and disable.")
    @GetMapping("/2Fa/configure")
    public Payload<TwoFactorResponse> twoFactorSetup(TwoFactorRequest request) {
        return twoFactorService.setup(request);
    }
}
