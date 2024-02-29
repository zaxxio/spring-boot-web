package org.wsd.app.security.auth.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
public class SignInRequest {
    @Email
    private String username;
    @NotEmpty
    private String password;
    private String code;
}
