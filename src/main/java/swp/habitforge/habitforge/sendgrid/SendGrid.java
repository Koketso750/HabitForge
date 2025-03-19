package swp.habitforge.habitforge.sendgrid;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class SendGrid {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    private void addInlineAttachment(Mail mail, String filePath, String contentId, String mimeType) throws IOException, URISyntaxException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(getClass().getResource(filePath).toURI()));
        String base64File = Base64.getEncoder().encodeToString(fileBytes);

        Attachments attachments = new Attachments();
        attachments.setFilename(filePath.substring(filePath.lastIndexOf("/") + 1));
        attachments.setType(mimeType);
        attachments.setDisposition("inline");
        attachments.setContentId(contentId);
        attachments.setContent(base64File);
        mail.addAttachments(attachments);
    }
}

