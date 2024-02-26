package org.wsd.app.security.auth.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignInRequest {
    @Email
    private String username;
    @NotEmpty
    private String password;
    private String code;
}
