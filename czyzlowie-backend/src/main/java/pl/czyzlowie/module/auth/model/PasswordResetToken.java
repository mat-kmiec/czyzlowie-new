package pl.czyzlowie.module.auth.model;

import jakarta.persistence.*;
import lombok.*;
import pl.czyzlowie.module.user.model.User;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_reset_token", indexes = {
    @Index(name = "idx_password_reset_token_token", columnList = "token"),
    @Index(name = "idx_password_reset_token_user_id", columnList = "user_id"),
    @Index(name = "idx_password_reset_token_expiry", columnList = "expiry_date")
})
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
