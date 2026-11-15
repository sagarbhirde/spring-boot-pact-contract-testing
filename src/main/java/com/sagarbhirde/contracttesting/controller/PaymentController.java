package com.sagarbhirde.contracttesting.controller;

import com.sagarbhirde.contracttesting.model.PaymentOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final Map<String, PaymentOrder> orderStore = new ConcurrentHashMap<>();

    public PaymentController() {
        // Pre-populate sample order for contract testing state
        orderStore.put("ORD-98765", new PaymentOrder(
                "ORD-98765",
                "CUST-1001",
                new BigDecimal("250.75"),
                "USD",
                "COMPLETED",
                Instant.parse("2026-11-15T10:00:00Z")
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrder(@PathVariable String orderId) {
        PaymentOrder order = orderStore.get(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PaymentOrder> createPaymentOrder(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.getOrDefault("orderId", "ORD-" + System.currentTimeMillis());
        String customerId = (String) request.get("customerId");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String currency = (String) request.getOrDefault("currency", "USD");

        PaymentOrder newOrder = new PaymentOrder(orderId, customerId, amount, currency, "PENDING", Instant.now());
        orderStore.put(orderId, newOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }
}
