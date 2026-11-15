# Enterprise Microservices API Governance with Pact Contract Testing

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Pact](https://img.shields.io/badge/Contract%20Testing-Pact%20JVM-blue.svg)](https://pact.io)
[![CI Pipeline](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blueviolet.svg)](.github/workflows/ci.yml)

This reference project demonstrates **Consumer-Driven Contract Testing (CDCT)** in a cloud-native microservices ecosystem. It illustrates how decoupled engineering squads can validate HTTP/REST API contracts independently without maintaining fragile, slow, and expensive end-to-end integration test environments.

---

## 🏗️ Architectural Overview & Workflow

```mermaid
sequenceDiagram
    autonumber
    participant Consumer as Checkout Service (Consumer)
    participant PactBroker as Pact Broker / Contract Repo
    participant Provider as Payment Service (Provider)
    
    Note over Consumer: 1. Consumer Test Phase
    Consumer->>Consumer: Run JUnit 5 Pact Consumer Test
    Consumer->>PactBroker: Publish generated Pact JSON contract
    
    Note over Provider: 2. Provider Verification Phase (CI/CD)
    Provider->>PactBroker: Fetch latest Consumer Contracts
    Provider->>Provider: Run Provider Verification Test against mock state
    Provider->>PactBroker: Publish verification results (can-i-deploy gate)
    
    Note over Provider,Consumer: 3. Zero-Breaking Production Release
```

---

## ⚡ Key Highlights & Production Standards

- **Modern JVM Core:** Built on **Java 21 LTS** utilizing Records for immutable data models and Generational ZGC runtime tuning.
- **Consumer-Driven Design:** The consumer specifies the exact contract payload expectations (`PaymentConsumerContractTest.java`).
- **Provider Verification:** The provider verifies the contract against its active HTTP controller endpoints (`PaymentProviderVerificationTest.java`).
- **CI/CD Integration Gate (`can-i-deploy`):** Automated GitHub Actions pipeline builds the project and executes contract verification suites on every push.
- **Containerization:** Multi-stage `Dockerfile` optimizing build caching and slim JRE 21 runtime container footprint.

---

## 🚀 Running Locally

### Prerequisites
- JDK 21+
- Apache Maven 3.9+
- Docker (optional)

### Build and Run Contract Tests
```bash
# Execute unit and Pact contract verification tests
mvn clean verify
```

Generated contracts will be written to `target/pacts/CheckoutServiceConsumer-PaymentServiceProvider.json`.

---

## 👨‍💻 Author & Maintainer
**Sagar Bhirde**  
Lead Java & Cloud Engineer  
[LinkedIn](https://www.linkedin.com/in/sagar-bhirde/) | [Email](mailto:sagarbhirde111@gmail.com)
