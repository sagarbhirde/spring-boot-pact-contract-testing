package com.sagarbhirde.contracttesting;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "PaymentServiceProvider")
public class PaymentConsumerContractTest {

    @Pact(consumer = "CheckoutServiceConsumer")
    public RequestResponsePact createPactForGetPayment(PactDslWithProvider builder) {
        return builder
                .given("Order ORD-98765 exists in completed state")
                .uponReceiving("A request to fetch payment order details")
                .path("/api/v1/payments/ORD-98765")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(newJsonBody(body -> {
                    body.stringValue("orderId", "ORD-98765");
                    body.stringValue("customerId", "CUST-1001");
                    body.numberValue("amount", 250.75);
                    body.stringValue("currency", "USD");
                    body.stringValue("status", "COMPLETED");
                }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPactForGetPayment")
    void testGetPaymentOrder(au.com.dius.pact.consumer.MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(mockServer.getUrl()).build();
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/payments/ORD-98765", Map.class);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ORD-98765", response.getBody().get("orderId"));
        assertEquals("COMPLETED", response.getBody().get("status"));
    }
}
