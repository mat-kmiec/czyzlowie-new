package pl.czyzlowie.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.czyzlowie.module.auth.model.VerificationToken;
import pl.czyzlowie.module.auth.repository.VerificationTokenRepository;
import pl.czyzlowie.module.user.repository.UserRepository;
import pl.czyzlowie.exception.UserNotFoundException;
import pl.czyzlowie.exception.TokenRefreshException;

import java.time.Instant;
import java.util.Random;

/** Service class for managing verification tokens. */
@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    /** Verification token duration in milliseconds. */
    @Value("${application.security.verification-token.expiration}")
    private long verificationTokenDurationMs;

    /** Create verification token for user.
     * @param userId user id
     * @return VerificationToken
     * @throws UserNotFoundException if user is not found*/
    @Transactional
    public VerificationToken createVerificationToken(Long userId) {
        VerificationToken token = new VerificationToken();
        token.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        token.setExpiryDate(Instant.now().plusMillis(verificationTokenDurationMs));
        token.setToken(generateRandomCode());
        
        return verificationTokenRepository.save(token);
    }

    /** Verify verification token.
     * @param email user email
     * @param verificationCode verification code
     * @throws UserNotFoundException if user is not found
     * @throws TokenRefreshException if verification token is not found, expired or invalid*/
    @Transactional
    public void verifyToken(String email, String verificationCode) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        
        var verificationToken = verificationTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new TokenRefreshException("Verification token not found"));
        
        if (!verificationToken.getToken().equals(verificationCode)) {
            throw new TokenRefreshException("Invalid verification code");
        }
        
        if (verificationToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            verificationTokenRepository.delete(verificationToken);
            throw new TokenRefreshException("Verification code has expired");
        }
        
        user.setIsVerified(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

    /** Generate random verification code.
     * @return random verification code*/
    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    /** Delete expired verification tokens.
     * Cron expression: At 03:00 on every day-of-month.*/
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredTokens(){
        verificationTokenRepository.deleteAllExpiredTokens(Instant.now());
    }
}

