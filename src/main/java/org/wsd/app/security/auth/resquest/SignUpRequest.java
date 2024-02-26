package org.wsd.app.security.auth.resquest;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SignUpRequest {
    @Email(message = "User must provide their email address.")
    @NotEmpty(message = "Username can not be blank.")
    private String username;
    @Size(min = 6, message = "Password must contain at least 6 characters.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{3,}$",
            message = "Password must have one uppercase letter, one lowercase letter, at least 3 digits, and one special character."
    )
    @NotEmpty
    private String password;
}
