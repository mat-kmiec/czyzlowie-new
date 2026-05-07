package pl.czyzlowie.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.czyzlowie.module.auth.dto.response.AuthResponse;
import pl.czyzlowie.module.auth.dto.request.LoginRequest;
import pl.czyzlowie.module.auth.dto.request.RegisterRequest;
import pl.czyzlowie.module.auth.dto.response.RegisterResponse;
import pl.czyzlowie.module.auth.dto.request.TokenRefreshRequest;
import pl.czyzlowie.module.auth.dto.request.EmailVerificationRequest;
import pl.czyzlowie.module.auth.model.RefreshToken;
import pl.czyzlowie.module.user.model.Role;
import pl.czyzlowie.module.user.model.User;
import pl.czyzlowie.module.user.repository.UserRepository;
import pl.czyzlowie.module.email.service.EmailService;
import pl.czyzlowie.exception.UserAlreadyExistsException;
import pl.czyzlowie.exception.UserNotFoundException;
import pl.czyzlowie.exception.TokenRefreshException;

/** Service class for user authentication and token generation. */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    /** Register new user.
     * @param request RegisterRequest
     * @return RegisterResponse
     * @throws UserAlreadyExistsException if user with given email already exists
     */
    public RegisterResponse register(RegisterRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        var user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isVerified(false)
                .build();
        repository.save(user);

        var verificationToken = verificationTokenService.createVerificationToken(user.getId());

        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken(), user.getNickname());
        
        return RegisterResponse.builder()
                .message("Registration successful. Please check your email for verification code.")
                .email(user.getEmail())
                .status("PENDING_VERIFICATION")
                .build();
    }

    /** Verify user email.
     * @param request EmailVerificationRequest
     */
    public void verifyEmail(EmailVerificationRequest request) {
        verificationTokenService.verifyToken(request.getEmail(), request.getVerificationCode());
    }

    /** Authenticate user and generate JWT token.
     * @param request LoginRequest
     * @return AuthResponse with access token and refresh token in request
     * @throws UserNotFoundException if user is not found
     */
    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if (!user.getIsVerified()) {
            throw new UserNotFoundException("Email not verified. Please verify your email first.");
        }
                
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    /** Refresh access token with new refresh token.
     * @param request TokenRefreshRequest
     * @return AuthResponse with new access token and refresh token in request
     * @throws TokenRefreshException if refresh token is not in database, expired, invalid, not found, user not verified or user is banned
     */
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return AuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
    }
}
