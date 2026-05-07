package pl.czyzlowie.module.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.czyzlowie.module.auth.model.VerificationToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUserId(Long userId);

    @Query("DELETE FROM VerificationToken vt WHERE vt.expiryDate < :now")
    void deleteAllExpiredTokens(Instant now);
}

