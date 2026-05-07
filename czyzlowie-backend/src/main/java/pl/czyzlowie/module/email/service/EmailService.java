package pl.czyzlowie.module.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.email.from:noreply@czyzlowie.com}")
    private String fromEmail;


    private void sendTemplatedEmail(String toEmail, String subject, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariables(variables);
            String htmlBody = templateEngine.process("email/layout", context);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send templated email: " + e.getMessage(), e);
        }
    }

    public void sendVerificationEmail(String toEmail, String verificationCode, String firstName) {
        Map<String, Object> templateVariables = Map.of(
                "subject", "Email Verification - Czyzlowie.pl",
                "name", firstName,
                "message", "Dziękujemy za rejestrację w Czyzlowie!<br>Aby aktywować swoje konto, użyj poniższego kodu weryfikacyjnego:",
                "code", verificationCode,
                "disclaimer", "Ten kod wygaśnie za 24 godziny."
        );

        sendTemplatedEmail(toEmail, "Email Verification - Czyzlowie", templateVariables);
    }

    public void sendPasswordResetEmail(String toEmail, String resetCode, String nickname) {
        Map<String, Object> templateVariables = Map.of(
                "subject", "Resetowanie hasła - Czyzlowie.pl",
                "name", nickname,
                "message", "Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta.<br>Użyj poniższego kodu, aby ustawić nowe hasło:",
                "code", resetCode,
                "disclaimer", "Ten kod wygaśnie za 1 godzinę. Jeśli to nie Ty złożyłeś tę prośbę, zignoruj tego maila."
        );

        sendTemplatedEmail(toEmail, "Resetowanie hasła - Czyzlowie", templateVariables);
    }
}