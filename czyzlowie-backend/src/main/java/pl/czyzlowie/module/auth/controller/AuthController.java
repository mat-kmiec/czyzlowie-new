package pl.czyzlowie.module.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.czyzlowie.module.auth.dto.request.EmailVerificationRequest;
import pl.czyzlowie.module.auth.dto.request.LoginRequest;
import pl.czyzlowie.module.auth.dto.request.RegisterRequest;
import pl.czyzlowie.module.auth.dto.request.TokenRefreshRequest;
import pl.czyzlowie.module.auth.service.AuthenticationService;
import pl.czyzlowie.module.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PostMapping("/verify-email")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<String> verifyEmail(
            @Valid @RequestBody EmailVerificationRequest request
    ) {
        service.verifyEmail(request);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    @PostMapping("/login")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    @RateLimiter(name = "authRateLimiter")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return ResponseEntity.ok("Log out successful!");
    }
}
