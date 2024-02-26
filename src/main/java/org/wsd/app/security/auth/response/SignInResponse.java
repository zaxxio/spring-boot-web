package org.wsd.app.security.auth.response;

import lombok.Data;

@Data
public class SignInResponse {
    private String accessToken;
    private String tokenType;
}
