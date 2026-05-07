package pl.czyzlowie.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.czyzlowie.exception.TokenRefreshException;
import pl.czyzlowie.exception.UserNotFoundException;
import pl.czyzlowie.module.auth.model.PasswordResetToken;
import pl.czyzlowie.module.auth.repository.PasswordResetTokenRepository;
import pl.czyzlowie.module.user.repository.UserRepository;

import java.time.Instant;
import java.util.Random;

/** Service class for managing password reset tokens. */
@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    /** Password reset token duration in milliseconds. */
    @Value("${application.security.password-reset-token.expiration:3600000}")
    private long passwordResetTokenDurationMs;

    /** Create password reset token for user.
     * @param userId user id
     * @return PasswordResetToken
     * @throws UserNotFoundException if user is not found*/
    @Transactional
    public PasswordResetToken createPasswordResetToken(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        passwordResetTokenRepository.deleteAllByUserId(userId);

        var token = PasswordResetToken.builder()
                .user(user)
                .token(generateRandomCode())
                .expiryDate(Instant.now().plusMillis(passwordResetTokenDurationMs))
                .build();

        return passwordResetTokenRepository.save(token);
    }

    /** Verify password reset token.
     * @param userId user id
     * @param resetCode reset code
     * @return PasswordResetToken if valid
     * @throws TokenRefreshException if token is not found, expired or invalid*/
    @Transactional(readOnly = true)
    public PasswordResetToken verifyToken(Long userId, String resetCode) {
        var token = passwordResetTokenRepository.findByTokenAndUserId(resetCode, userId)
                .orElseThrow(() -> new TokenRefreshException("Invalid password reset code"));

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException("Password reset code has expired. Please request a new one.");
        }

        return token;
    }

    /** Delete password reset token after successful password reset.
     * @param tokenId token id*/
    @Transactional
    public void deleteToken(Long tokenId) {
        passwordResetTokenRepository.deleteById(tokenId);
    }

    /** Delete all password reset tokens for user.
     * @param userId user id*/
    @Transactional
    public void deleteAllByUserId(Long userId) {
        passwordResetTokenRepository.deleteAllByUserId(userId);
    }

    /** Generate random 6-digit verification code.
     * @return random verification code*/
    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    /** Delete expired password reset tokens.
     * Cron expression: At 03:00 on every day-of-month.*/
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteAllExpiredTokens(Instant.now());
    }
}
