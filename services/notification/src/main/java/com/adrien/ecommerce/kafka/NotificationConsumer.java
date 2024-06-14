package com.adrien.ecommerce.kafka;

import com.adrien.ecommerce.email.EmailService;
import com.adrien.ecommerce.kafka.order.OrderConfirmation;
import com.adrien.ecommerce.kafka.payment.PaymentConfirmation;
import com.adrien.ecommerce.notification.Notification;
import com.adrien.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.adrien.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;
import static com.adrien.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;
import static java.lang.String.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;


    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );

       var customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
       emailService.sendPaymentSuccessEmail(
               paymentConfirmation.customerEmail(),
               customerName,
               paymentConfirmation.amount(),
               paymentConfirmation.orderReference()
       );
    };

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(format("Consuming the message from order-topic Topic:: %s", orderConfirmation));
        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        var customerName = orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    };

}
