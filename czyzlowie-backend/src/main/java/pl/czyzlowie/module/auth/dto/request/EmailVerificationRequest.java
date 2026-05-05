package pl.czyzlowie.module.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequest {
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Verification code is required")
    private String verificationCode;
}

