package org.wsd.app.security.auth.impl;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wsd.app.domain.UserEntity;
import org.wsd.app.payload.Payload;
import org.wsd.app.repository.UserRepository;
import org.wsd.app.security.auth.response.TwoFactorResponse;
import org.wsd.app.security.auth.resquest.TwoFactorRequest;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwoFactorService {

    private final UserRepository userRepository;

    public String generateNewSecret() {
        return new DefaultSecretGenerator().generate();
    }

    public String generateQrCodeImageUri(String secret, String username) {
        QrData data = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer("WALL STREET DOCS - WSD")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];
        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            e.printStackTrace();
            log.error("Error while generating QR-CODE");
        }

        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }

    public boolean isOtpNotValid(String secret, String code) {
        return !this.isOtpValid(secret, code);
    }


    public Payload<TwoFactorResponse> setup(TwoFactorRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserEntityByUsername(username)
                .map(user -> {
                    user.set2FAEnabled(request.getIs2FAEnabled());
                    if (request.getIs2FAEnabled()) {
                        if (user.getSecret() == null) {
                            user.setSecret(generateNewSecret());
                        }
                    }
                    return updateUserAndCreateResponse(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Payload<TwoFactorResponse> updateUserAndCreateResponse(UserEntity user) {
        UserEntity persistedUser = userRepository.save(user);
        TwoFactorResponse twoFactorResponse = new TwoFactorResponse();
        twoFactorResponse.setMFaEnabled(persistedUser.is2FAEnabled());

        if (persistedUser.is2FAEnabled()) {
            twoFactorResponse.setSecretImageURI(generateQrCodeImageUri(persistedUser.getSecret(), persistedUser.getUsername()));
        } else {
            twoFactorResponse.setSecretImageURI("");
        }

        return new Payload.Builder<TwoFactorResponse>()
                .payload(twoFactorResponse)
                .message("Two factor authentication " + (persistedUser.is2FAEnabled() ? "enabled" : "disabled") + " for " + user.getUsername())
                .build();
    }

}