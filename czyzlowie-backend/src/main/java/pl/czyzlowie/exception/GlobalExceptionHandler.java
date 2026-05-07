package pl.czyzlowie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestNotPermitted.class)
    public ProblemDetail handleRequestNotPermitted(RequestNotPermitted ex) {
        return createProblemDetail(HttpStatus.TOO_MANY_REQUESTS, "Too many requests - please try again later.", "Rate Limit Exceeded");
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ProblemDetail handleTokenRefreshException(TokenRefreshException ex) {
        return createProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), "Token Refresh Failed");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return createProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "User Already Exists");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "User Not Found");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "Invalid verify or password", "Authentication Failed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, "Validation Failed", "Invalid Input Data");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "Internal Server Error");
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
