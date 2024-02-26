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
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Payload<SignInResponse> signIn(SignInRequest signInRequest) throws TwoFactorFailedException {

        Optional<UserEntity> userEntity = userRepository.findUserEntityByUsername(signInRequest.getUsername());
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("Username not found in the database");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword(), userEntity.get().getAuthorities());
        Authentication authenticated = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        final SignInResponse signInResponse = new SignInResponse();

        Instant now = Instant.now();
        Instant validity = now.plus(jwtConfig.getExpiration(), ChronoUnit.MINUTES);

        if (userEntity.get().is2FAEnabled()) {
            if (signInRequest.getCode() != null && twoFactorService.isOtpNotValid(userEntity.get().getSecret(), signInRequest.getCode())) {
                throw new TwoFactorFailedException("Valid two factor authentication required : " + userEntity.get().getUsername());
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

        RoleEntity role = new RoleEntity();
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
