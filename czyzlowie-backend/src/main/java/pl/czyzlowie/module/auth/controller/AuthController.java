package pl.czyzlowie.module.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czyzlowie.module.auth.dto.request.EmailVerificationRequest;
import pl.czyzlowie.module.auth.dto.request.ForgotPasswordRequest;
import pl.czyzlowie.module.auth.dto.request.LoginRequest;
import pl.czyzlowie.module.auth.dto.request.RegisterRequest;
import pl.czyzlowie.module.auth.dto.request.ResetPasswordRequest;
import pl.czyzlowie.module.auth.dto.request.TokenRefreshRequest;
import pl.czyzlowie.module.auth.service.AuthenticationService;
import pl.czyzlowie.module.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;

@Tag(name = "Authentication", description = "Endpoints for user authentication, registration, and token management")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and sends a verification email. Email must be unique and password must meet security requirements."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully. Verification email sent.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(value = "{\"message\": \"Registration successful. Please check your email to verify your account.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - validation failed",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid email format or password requirements not met\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with this email already exists",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"User already exists\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limit exceeded",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Rate limit exceeded\"}"))
            )
    })
    @PostMapping("/register")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    name = "Registration Example",
                                    value = "{\"email\": \"user@example.com\", \"password\": \"SecurePass123!\", \"firstName\": \"John\", \"lastName\": \"Doe\"}"
                            )
                    )
            )
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @Operation(
            summary = "Verify user email",
            description = "Verifies the user's email address using the verification token sent to their email. This must be completed before the user can log in."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email verified successfully",
                    content = @Content(examples = @ExampleObject(value = "Email verified successfully. You can now log in."))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid or expired verification token",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid or expired verification token\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"User not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limit exceeded"
            )
    })
    @PostMapping("/verify-email")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<String> verifyEmail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email verification token received via email",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EmailVerificationRequest.class),
                            examples = @ExampleObject(value = "{\"token\": \"abc123xyz456\"}")
                    )
            )
            @Valid @RequestBody EmailVerificationRequest request
    ) {
        service.verifyEmail(request);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user with email and password, returning access and refresh tokens. User must have verified their email before logging in."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Login Success",
                                    value = "{\"accessToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"refreshToken\": \"550e8400-e29b-41d4-a716-446655440000\", \"tokenType\": \"Bearer\", \"expiresIn\": 900000}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid email or password format\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials or email not verified",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid email or password\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many login attempts - rate limit exceeded"
            )
    })
    @PostMapping("/login")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = "{\"email\": \"user@example.com\", \"password\": \"SecurePass123!\"}")
                    )
            )
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Operation(
            summary = "Refresh JWT access token",
            description = "Generates a new access token using a valid refresh token. Use this when the access token expires (after 15 minutes)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Refresh Success",
                                    value = "{\"accessToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"refreshToken\": \"550e8400-e29b-41d4-a716-446655440000\", \"tokenType\": \"Bearer\", \"expiresIn\": 900000}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Refresh token is required\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid refresh token\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limit exceeded"
            )
    })
    @PostMapping("/refresh")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token obtained during login",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenRefreshRequest.class),
                            examples = @ExampleObject(value = "{\"refreshToken\": \"550e8400-e29b-41d4-a716-446655440000\"}")
                    )
            )
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @Operation(
            summary = "Logout user",
            description = "Logs out the user by invalidating their refresh token. After logout, the refresh token can no longer be used to generate new access tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful",
                    content = @Content(examples = @ExampleObject(value = "Log out successful!"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Refresh token is required\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid refresh token",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid refresh token\"}"))
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token to invalidate",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenRefreshRequest.class),
                            examples = @ExampleObject(value = "{\"refreshToken\": \"550e8400-e29b-41d4-a716-446655440000\"}")
                    )
            )
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return ResponseEntity.ok("Log out successful!");
    }

    @Operation(
            summary = "Request password reset",
            description = "Sends a 6-digit password reset code to the user's email address. The code is valid for 1 hour."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset code sent successfully",
                    content = @Content(examples = @ExampleObject(value = "Password reset code has been sent to your email address."))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid email format\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"User not found with email: user@example.com\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limit exceeded"
            )
    })
    @PostMapping("/forgot-password")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<String> forgotPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email address to send password reset code",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ForgotPasswordRequest.class),
                            examples = @ExampleObject(value = "{\"email\": \"user@example.com\"}")
                    )
            )
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(service.forgotPassword(request));
    }

    @Operation(
            summary = "Reset password with code",
            description = "Resets the user's password using the 6-digit code sent via email. All active sessions will be invalidated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successful",
                    content = @Content(examples = @ExampleObject(value = "Password has been reset successfully. Please login with your new password."))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format or password requirements not met",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid or expired reset code",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"Invalid password reset code\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(examples = @ExampleObject(value = "{\"error\": \"User not found with email: user@example.com\"}"))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limit exceeded"
            )
    })
    @PostMapping("/reset-password")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<String> resetPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password reset request with code and new password",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResetPasswordRequest.class),
                            examples = @ExampleObject(
                                    value = "{\"email\": \"user@example.com\", \"resetCode\": \"123456\", \"newPassword\": \"NewSecurePass123!\"}"
                            )
                    )
            )
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(service.resetPassword(request));
    }
}
