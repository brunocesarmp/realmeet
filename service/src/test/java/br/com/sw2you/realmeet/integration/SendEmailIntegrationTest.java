package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.TestUtils.sleep;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.email.EmailInfo;
import br.com.sw2you.realmeet.email.EmailSender;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

class SendEmailIntegrationTest extends BaseIntegrationTest {

    private static final String EMAIL_ADDRESS = "teste@gmail.com";
    private static final String SUBJECT = "Subject";
    private static final String EMAIL_TEMPLATE = "template-test.html";

    @Autowired
    private EmailSender emailSender;

    @MockBean
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Test
    void testSendEmail() {

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        var emailInfo = EmailInfo.builder()
                .from(EMAIL_ADDRESS)
                .to(List.of(EMAIL_ADDRESS))
                .subject(SUBJECT)
                .template(EMAIL_TEMPLATE)
                .templateData(Map.of("param", "some text"))
                .build();

        emailSender.send(emailInfo);
        sleep(1000L);
        verify(javaMailSender).send(eq(mimeMessage));
    }

}
