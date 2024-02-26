package org.wsd.app.security.auth.resquest;

import lombok.Data;

@Data
public class TwoFactorRequest {
    private Boolean is2FAEnabled;
}
