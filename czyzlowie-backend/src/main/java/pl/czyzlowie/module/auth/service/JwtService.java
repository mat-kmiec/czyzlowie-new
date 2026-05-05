package pl.czyzlowie.module.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/** Service class for JWT-related operations.*/
@Service
public class JwtService {

    /** Secret key for JWT signing. */
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    /** JWT expiration time. */
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /** Extract username from JWT token.
     * @param token JWT token
     * @return username
     * @throws IllegalArgumentException if token is invalid*/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Generate JWT token.
     * @param userDetails UserDetails
     * @return JWT token
     * */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    /** Check if JWT token is valid.
     * @param token JWT token
     * @param userDetails UserDetails
     * @return true if token is valid, false otherwise
     * */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /** Check if JWT token is expired.
     * @param token JWT token
     * @return true if token is expired, false otherwise
     * */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    } 

    /** Extract expiration date from JWT token.
     * @param token JWT token
     * @return expiration date
     * */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Extract claim from JWT token.
     * @param token JWT token
     * @param claimsResolver function to extract claim
     * @return claim
     * */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** Extract all claims from JWT token.
     * @param token JWT token
     * @return Claims
     * */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Get secret key for JWT signing.
     * @return SecretKey
     * */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
