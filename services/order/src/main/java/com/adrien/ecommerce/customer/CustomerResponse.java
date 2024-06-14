package com.adrien.ecommerce.customer;

public record CustomerResponse(
        String id,
        String lastname,
        String firstname,
        String email
) {
}
