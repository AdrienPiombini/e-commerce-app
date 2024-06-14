package com.adrien.ecommerce.payment;

import com.adrien.ecommerce.order.PaymentMethod;
import com.adrien.ecommerce.customer.CustomerResponse;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
