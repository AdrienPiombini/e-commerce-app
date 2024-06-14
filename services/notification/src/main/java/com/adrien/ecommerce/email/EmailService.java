package com.adrien.ecommerce.email;

import com.adrien.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adrien.ecommerce.email.EmailTemplate.ORDER_CONFIRMATION;
import static com.adrien.ecommerce.email.EmailTemplate.PAYMENT_CONFIRMATION;
import static java.lang.String.*;
import static java.nio.charset.StandardCharsets.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());
        messageHelper.setFrom("adrien.piombini@gmail.com");
        final String templateName = PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());

        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(format("INFO - Email successfully sent to %s with template %s, ", destinationEmail, templateName));
        }catch (MessagingException e){
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());
        messageHelper.setFrom("adrien.piombini@gmail.com");
        final String templateName = ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("total amount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);


        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(format("INFO - Email successfully sent to %s with template %s, ", destinationEmail, templateName));
        }catch (MessagingException e){
            log.warn("WARNING - Cannot send email to {}", destinationEmail);
        }
    }
}
