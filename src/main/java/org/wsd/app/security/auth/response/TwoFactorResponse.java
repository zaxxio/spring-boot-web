package org.wsd.app.security.auth.response;

import lombok.Data;

@Data
public class TwoFactorResponse {
    private Boolean mFaEnabled;
    private String secretImageURI;
}
