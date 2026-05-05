package pl.czyzlowie.module.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.czyzlowie.module.auth.model.RefreshToken;
import pl.czyzlowie.module.auth.repository.RefreshTokenRepository;
import pl.czyzlowie.module.user.repository.UserRepository;
import pl.czyzlowie.exception.TokenRefreshException;
import pl.czyzlowie.exception.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/** Service for refresh token operations. */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /** Refresh token duration in milliseconds. */
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenDurationMs;

    /** Create refresh token for user.
     * @param userId user id
     * @return RefreshToken
     * @throws UserNotFoundException if user is not found*/
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        
        return refreshTokenRepository.save(refreshToken);
    }

    /** Find refresh token by token.
     * @param token refresh token
     * @return Optional<RefreshToken>
     * Empty if refresh token is not found*/
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /** Verify if refresh token is expired.
     * @param token RefreshToken
     * @return RefreshToken if token is valid
     * @throws TokenRefreshException if token is expired
     * */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request: " + token.getToken());
        }
        return token;
    }

    /** Delete refresh token by token.
     * @param token refresh token
     * @throws TokenRefreshException if refresh token is not found
     */
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }


    /** Delete all expired refresh tokens.
     * Cron expression: 0 0 3 * * * (every day at 3:00 AM)*/
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredTokens(){
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }
}
