package swp.habitforge.habitforge.user;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PasswordResetService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${app.base.url}")
    private String appBaseUrl;

    // In-memory storage for OTPs (in production, use a cache like Redis)
    private final Map<String, OtpDetails> otpStorage = new HashMap<>();

    private static class OtpDetails {
        String otp;
        LocalDateTime expiryTime;

        OtpDetails(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    public void sendPasswordResetOtp(String email) throws IOException {
        // Generate 6-digit OTP
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plus(15, ChronoUnit.MINUTES);

        // Store OTP
        otpStorage.put(email, new OtpDetails(otp, expiryTime));

        // Send email
        Email from = new Email(fromEmail);
        String subject = "HabitForge - Password Reset OTP";
        Email to = new Email(email);
        Content content = new Content("text/html", buildEmailContent(otp));
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                throw new IOException("Failed to send email: " + response.getBody());
            }
        } catch (IOException ex) {
            throw new IOException("Error sending password reset email", ex);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        OtpDetails details = otpStorage.get(email);
        if (details == null) {
            return false;
        }

        // Check if OTP matches and isn't expired
        return details.otp.equals(otp) && LocalDateTime.now().isBefore(details.expiryTime);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    private String generateOtp() {
        Random random = new Random();
        int num = random.nextInt(999999);
        return String.format("%06d", num);
    }

    private String buildEmailContent(String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { color: #4CAF50; text-align: center; }" +
                ".otp-box { background: #f4f4f4; padding: 10px; margin: 20px 0; text-align: center; font-size: 24px; font-weight: bold; }" +
                ".footer { margin-top: 20px; font-size: 12px; text-align: center; color: #777; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1 class='header'>HabitForge Password Reset</h1>" +
                "<p>You requested to reset your password. Please use the following OTP to proceed:</p>" +
                "<div class='otp-box'>" + otp + "</div>" +
                "<p>This OTP is valid for 15 minutes. If you didn't request this, please ignore this email.</p>" +
                "<div class='footer'>" +
                "<p>© 2025 HabitForge. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}