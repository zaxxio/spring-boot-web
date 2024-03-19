/*
 * Copyright (c) of Partha Sutradhar 2024.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.wsd.app.security.auth.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.RoleEntity;
import org.wsd.app.domain.UserEntity;
import org.wsd.app.payload.Payload;
import org.wsd.app.repository.UserRepository;
import org.wsd.app.security.auth.AuthenticationService;
import org.wsd.app.security.auth.response.SignInResponse;
import org.wsd.app.security.auth.response.SignUpResponse;
import org.wsd.app.security.auth.resquest.SignInRequest;
import org.wsd.app.security.auth.resquest.SignUpRequest;
import org.wsd.app.security.jwt.JwtConfig;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorService twoFactorService;

    @Override
    //@Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Payload<SignInResponse> signIn(SignInRequest signInRequest) throws TwoFactorFailedException, UsernameNotFoundException {

        UserEntity user = userRepository.findUserEntityByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(signInRequest.getUsername() + " not found in the database."));

        if (user.is2FAEnabled() && signInRequest.getCode() == null) {
            throw new TwoFactorFailedException("Two Factor Authentication code is required.");
        }

        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),
                signInRequest.getPassword(),
                user.getAuthorities()
        );
        Authentication authenticated = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        final SignInResponse signInResponse = new SignInResponse();

        Instant now = Instant.now();
        Instant validity = now.plus(jwtConfig.getExpiration(), ChronoUnit.MINUTES);

        if (user.is2FAEnabled()) {
            if (signInRequest.getCode() != null && twoFactorService.isOtpNotValid(user.getSecret(), signInRequest.getCode())) {
                throw new TwoFactorFailedException("Valid two factor authentication required : " + user.getUsername());
            }
        }

        // Convert authorities to a space-separated string for the scope claim
        String scopes = authenticated.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        final JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authenticated.getName())
                // Use "scp" or "scope" as the claim name for scopes, depending on your convention
                .claim("scp", scopes) // or .claim("scope", scopes)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
        String jwtToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, accessTokenClaims)).getTokenValue();
        signInResponse.setAccessToken(jwtToken);
        signInResponse.setTokenType("Bearer");
        return new Payload.Builder<SignInResponse>()
                .message("Generated Access Token")
                .payload(signInResponse)
                .build();
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    @Override
    public Payload<SignUpResponse> signUp(@Valid SignUpRequest signUpRequest) {

        final UserEntity user = new UserEntity();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.set2FAEnabled(false);

        final RoleEntity role = new RoleEntity();
        role.setName("USER");
        user.setRoleEntities(Set.of(role));

        // saving the data
        UserEntity persistedUser = userRepository.save(user);

        final SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setUserId(persistedUser.getUserId());
        signUpResponse.setUsername(persistedUser.getUsername());

        return new Payload.Builder<SignUpResponse>()
                .message("User created")
                .payload(signUpResponse)
                .build();
    }
}
