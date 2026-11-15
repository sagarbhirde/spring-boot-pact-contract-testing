package com.sagarbhirde.contracttesting.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Modern Java 21 Record representing a high-scale payment transaction order.
 */
public record PaymentOrder(
        String orderId,
        String customerId,
        BigDecimal amount,
        String currency,
        String status,
        Instant timestamp
) {
    public PaymentOrder {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }
}
