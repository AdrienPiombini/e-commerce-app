package com.adrien.ecommerce.kafka;

import com.adrien.ecommerce.customer.CustomerResponse;
import com.adrien.ecommerce.order.PaymentMethod;
import com.adrien.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
