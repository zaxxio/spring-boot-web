package org.wsd.app.security.auth.response;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class SignUpResponse {
    private UUID userId;
    private String username;
}
