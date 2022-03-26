package br.com.sw2you.realmeet.email;

import static br.com.sw2you.realmeet.util.StringUtils.join;
import static java.util.Objects.nonNull;
import static javax.mail.Message.RecipientType.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import br.com.sw2you.realmeet.exception.EmailSendingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);
    private static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";

    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;

    public EmailSender(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void send(EmailInfo emailInfo) {
        LOG.info("Sending email with subject: {} to {}", emailInfo.getSubject(), emailInfo.getTo());

        var mimeMessage = javaMailSender.createMimeMessage();
        var multipart = new MimeMultipart();

        addBasicDetails(emailInfo, mimeMessage);
        addHtmlBody(emailInfo.getTemplate(), emailInfo.getTemplateData(), multipart);
        addAttachments(emailInfo.getAttachments(), multipart);
        setContent(mimeMessage, multipart);

        javaMailSender.send(mimeMessage);

    }

    private void addBasicDetails(EmailInfo emailInfo, MimeMessage mimeMessage) {
        try {
            mimeMessage.setFrom(emailInfo.getFrom());
            mimeMessage.setSubject(emailInfo.getSubject());
            mimeMessage.addRecipients(TO, join(emailInfo.getTo()));

            if (nonNull(emailInfo.getCc())) {
                mimeMessage.addRecipients(CC, join(emailInfo.getCc()));
            }

            if (nonNull(emailInfo.getBcc())) {
                mimeMessage.addRecipients(BCC, join(emailInfo.getBcc()));
            }
        } catch (MessagingException exception) {
            throwEmailSendingException(exception, "Error adding data to MIME Message");
        }
    }

    private void addHtmlBody(String template, Map<String, Object> templateData, MimeMultipart multipart) {
        var messageHtmlPart = new MimeBodyPart();
        var context = new Context();

        if (nonNull(templateData)) {
            context.setVariables(templateData);
        }

        try {
            messageHtmlPart.setContent(templateEngine.process(template, context), TEXT_HTML_CHARSET_UTF_8);
            multipart.addBodyPart(messageHtmlPart);
        } catch (MessagingException e) {
            throwEmailSendingException(e, "Error adding html content to MIME Message");
        }
    }

    private void addAttachments(List<Attachment> attachments, MimeMultipart multipart) {
        if (nonNull(attachments)) {
            attachments.forEach((attachment -> {
                try {
                    var messageAttachmentPart = new MimeBodyPart();
                    messageAttachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getInputStream(), attachment.getContentType())));
                    messageAttachmentPart.setFileName(attachment.getFileName());
                    multipart.addBodyPart(messageAttachmentPart);
                } catch (MessagingException | IOException e) {
                    throwEmailSendingException(e, "Error adding attachments to MIME Message");
                }
            }));
        }
    }

    private void setContent(MimeMessage mimeMessage, MimeMultipart multipart) {
        try {
            mimeMessage.setContent(multipart);
        } catch (MessagingException e) {
            throwEmailSendingException(e, "Error setting content to MIME Message");
        }
    }

    private void throwEmailSendingException(Exception exception, String errorMessage) {
        var fullErrorMessage = String.format("%s: %s", exception.getMessage(), errorMessage);
        LOG.error(fullErrorMessage);
        throw new EmailSendingException(errorMessage, exception);
    }

}
