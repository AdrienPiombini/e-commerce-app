package com.adrien.ecommerce.orderline;

public record OrderLineRequest(Integer id, Integer orderId, Integer productId, double quantity) {
}
