package pl.czyzlowie.module.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration response")
public class RegisterResponse {

    @Schema(description = "Success message", example = "Registration successful. Please check your email for verification code.")
    private String message;

    @Schema(description = "Registered email address", example = "user@example.com")
    private String email;

    @Schema(description = "Registration status", example = "PENDING_VERIFICATION")
    private String status;
}

