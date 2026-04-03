# Order Processing System

![Status](https://img.shields.io/badge/status-in%20progress-yellow)
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-green?logo=springboot)
![Go](https://img.shields.io/badge/Go-1.22-00ADD8?logo=go)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-7.4-231F20?logo=apachekafka)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)

Event-driven order processing system simulating a food delivery backend. Built with Java/Spring Boot, Apache Kafka, and Go microservices. Future deploy on VPS with Docker.

---

## Architecture

```
Client → Spring Boot (REST API) → Kafka → Go Services → Kafka → Spring Boot (state update) → PostgreSQL
```

| Service | Language | Responsibility |
|---|---|---|
| `order-service` | Java / Spring Boot | REST API, persistence, Kafka producer & consumer |
| `payment-service` | Go | Consumes `order.created`, emits `payment.processed` |
| `preparation-service` | Go | Consumes `payment.processed`, emits `order.prepared` |
| `delivery-service` | Go | Consumes `order.prepared`, emits `order.delivered` |

### Event flow

```
order.created
    └── payment-service
            └── payment.processed
                    └── preparation-service
                                └── order.prepared
                                        └── delivery-service
                                                └── order.delivered
                                                        └── Spring Boot → DB (DELIVERED)
```

### Order lifecycle

```
CREATED → PAYMENT_PENDING → PAYMENT_APPROVED → PREPARING → READY → DELIVERED
                                    └── PAYMENT_REJECTED
```

---

## Project structure

```
order-processing/
├── order-service/           # Spring Boot — API REST + Kafka producer/consumer
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── payment-service/         # Go — payment processor
│   ├── cmd/
│   ├── internal/
│   ├── go.mod
│   └── Dockerfile
├── preparation-service/     # Go — preparation simulator
│   ├── cmd/
│   ├── internal/
│   ├── go.mod
│   └── Dockerfile
├── delivery-service/        # Go — delivery simulator
│   ├── cmd/
│   ├── internal/
│   ├── go.mod
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Stack

- **Java 21** + **Spring Boot 4.0.5** — REST API, JPA, Kafka integration
- **Go 1.22** — lightweight event consumers/producers
- **Apache Kafka** — event bus (via Confluent Platform 7.4)
- **PostgreSQL 15** — single source of truth, managed only by Spring Boot
- **Docker + Docker Compose** — local and production environment

---

## Getting started

### Prerequisites

- Docker and Docker Compose installed
- Java 21 (for local development without Docker)
- Go 1.22 (for local development without Docker)

### Run with Docker Compose

```bash
git clone https://github.com/cacamilo0/order-processing.git
cd order-processing
cp .env.example .env        # set your DB credentials
docker-compose up --build
```

All services start automatically. Kafka and PostgreSQL healthchecks ensure correct startup order.

### Verify it works

```bash
# Create an order
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "items": [
      { "productId": "abc", "name": "Hamburguesa", "quantity": 2, "price": 8.50 }
    ]
  }'

# Check order status
curl http://localhost:8080/api/v1/orders/{orderId}
```

---

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/orders` | Create a new order |
| `GET` | `/api/v1/orders/{id}` | Get order by ID |
| `GET` | `/api/v1/orders?customerId=` | List orders by customer |
| `GET` | `/api/v1/health` | Health check |

---

## Kafka topics

| Topic | Producer | Consumer |
|---|---|---|
| `order.created` | order-service | payment-service |
| `payment.processed` | payment-service | order-service, preparation-service |
| `order.prepared` | preparation-service | order-service, delivery-service |
| `order.delivered` | delivery-service | order-service |

---

## Development progress

- [x] Spring Boot project setup
- [x] PostgreSQL schema (`op` schema, Flyway migrations)
- [x] REST endpoints — create and query orders
- [x] DTO validations - global exception handler
- [x] PostgreSQL run with Docker with init script
- [ ] Kafka integration in Spring Boot
- [ ] Go payment-service
- [ ] Go preparation-service
- [ ] Go delivery-service
- [ ] Full E2E flow verified locally
- [ ] Dockerfiles for all services
- [ ] Deploy to VPS

---

## Author

**cacamilo0** — [github.com/cacamilo0](https://github.com/cacamilo0)