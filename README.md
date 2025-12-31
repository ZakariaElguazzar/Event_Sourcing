# Event Sourcing & CQRS Banking Application

A comprehensive **Event Sourcing** and **CQRS** (Command Query Responsibility Segregation) implementation for managing bank accounts, built with **Spring Boot** and **Axon Framework**.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
    - [Event Sourcing](#event-sourcing)
    - [CQRS Pattern](#cqrs-pattern)
- [Key Concepts](#key-concepts)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Technology Stack](#technology-stack)
- [Docker Setup](#docker-setup)

---

## 🎯 Overview

This project demonstrates a modern approach to building event-driven applications using **Event Sourcing** and **CQRS** patterns. It implements a simple banking domain where users can:

- Create accounts
- Credit/debit accounts
- Update account status
- Query account information and transaction history
- Watch real-time account operations

The application showcases how to maintain a complete audit trail of all changes through events while optimizing read and write operations separately.

---

## 🏗️ Architecture

### Event Sourcing

**Event Sourcing** is a pattern where state changes are stored as a sequence of events. Instead of storing just the current state, every change is captured as an immutable event.

#### How it works in this project:

1. **Commands** are sent to modify account state (e.g., `CreditAccountCmd`)
2. **Aggregates** (`AccountAggregate`) validate business rules and emit **Events**
3. **Events** (e.g., `AccountCreditedEvent`) are stored in the **Event Store** (managed by Axon Server)
4. The aggregate state is reconstructed by replaying all events

**Benefits:**
- ✅ Complete audit trail of all changes
- ✅ Time travel - reconstruct state at any point in time
- ✅ Event replay for debugging and analysis
- ✅ Support for event-driven integrations

**Example Flow:**
```
CreditAccountCmd → AccountAggregate → AccountCreditedEvent → Event Store
                                                           ↓
                                                    Event Handlers
```


### CQRS Pattern

**CQRS** (Command Query Responsibility Segregation) separates read and write operations into different models.

#### Command Side (Write Model):
- Handles business operations (create, credit, debit, update)
- Uses **Aggregates** to enforce business rules
- Emits **Events** to record changes
- Optimized for consistency and validation

#### Query Side (Read Model):
- Handles data retrieval operations
- Uses **JPA Entities** and **Repositories** for fast queries
- Updated by **Event Handlers** listening to events
- Optimized for query performance

**Benefits:**
- ✅ Independent scaling of reads and writes
- ✅ Optimized data models for different use cases
- ✅ Simplified business logic
- ✅ Better performance

---

## 🔑 Key Concepts

### Aggregates
**`AccountAggregate`** - The core domain entity that:
- Receives commands via `@CommandHandler` methods
- Validates business rules
- Emits events using `AggregateLifecycle.apply()`
- Rebuilds state via `@EventSourcingHandler` methods

### Commands
Immutable objects representing user intentions:
- `AddAccountCmd` - Create a new account
- `CreditAccountCmd` - Add funds
- `DebitAccountCmd` - Withdraw funds
- `UpdateAccountCmd` - Change account status

### Events
Immutable facts about what happened:
- `AccountCreatedEvent` - Account was created
- `AccountCreditedEvent` - Funds were added
- `AccountDebitedEvent` - Funds were withdrawn
- `AccountUpdatedEvent` - Status changed

### Handlers
- **Command Handlers** - Process commands in aggregates
- **Event Handlers** - Update read models when events occur
- **Query Handlers** - Return data from read models

---

## 📁 Project Structure

```
src/main/java/org/example/event_sourcing/
├── commands/                          # Command Side (Write Model)
│   ├── aggregates/
│   │   └── AccountAggregate.java     # Core business logic
│   ├── cmds/                         # Command definitions
│   ├── controllers/
│   │   └── AccountCommandController.java
│   └── dto/                          # Request DTOs
├── events/                           # Domain Events
│   ├── AccountCreatedEvent.java
│   ├── AccountCreditedEvent.java
│   ├── AccountDebitedEvent.java
│   └── AccountUpdatedEvent.java
├── query/                            # Query Side (Read Model)
│   ├── controllers/
│   │   └── AccountQueryController.java
│   ├── entities/                     # JPA Entities
│   │   ├── Account.java
│   │   └── AccountOperation.java
│   ├── handlers/                     # Event & Query Handlers
│   │   ├── AccountEventHandler.java
│   │   └── AccountQueryHandler.java
│   ├── queries/                      # Query definitions
│   └── repositories/                 # JPA Repositories
└── enums/
    ├── AccountStatus.java
    └── OperationType.java
```


---

## ⚙️ Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **Docker & Docker Compose** (for Axon Server and PostgreSQL)
- **Git**

---

## 🚀 Getting Started

### 1. Clone the repository
```shell script
git clone <your-repo-url>
cd Event_Sourcing
```


### 2. Start infrastructure services
```shell script
docker-compose up -d
```


This starts:
- **Axon Server** (ports 8024, 8124) - Event Store & Message Routing
- **PostgreSQL** (port 5432) - Read Model Database
- **pgAdmin** (port 8088) - Database Management UI

### 3. Build the application
```shell script
./mvnw clean package -DskipTests
```


### 4. Run the application
```shell script
./mvnw spring-boot:run
```


The application will start on **http://localhost:8080**

### 5. Access Axon Server Dashboard
Visit **http://localhost:8024** to view event store, command/query tracking, and system health.

---

## 🔌 API Endpoints

### Command Side (Write Operations)

#### Create Account
```shell script
POST /commands/accounts/create
Content-Type: application/json

{
  "accountHolderName": "John Doe",
  "initialBalance": 1000.0,
  "currency": "USD"
}
```


#### Credit Account
```shell script
POST /commands/accounts/credit
Content-Type: application/json

{
  "accountId": "abc-123",
  "amount": 500.0
}
```


#### Debit Account
```shell script
POST /commands/accounts/debit
Content-Type: application/json

{
  "accountId": "abc-123",
  "amount": 200.0
}
```


#### Update Account Status
```shell script
PUT /commands/accounts/update
Content-Type: application/json

{
  "accountId": "abc-123",
  "status": "ACTIVATED"
}
```


**Account Statuses:** `CREATED`, `ACTIVATED`, `SUSPENDED`, `BLOCKED`, `CLOSED`

#### Get Event History
```shell script
GET /commands/accounts/events/{accountId}
```

Returns the complete event stream for an account.

---

### Query Side (Read Operations)

#### Get All Accounts
```shell script
GET /query/accounts/all
```


#### Get Account Statement
```shell script
GET /query/accounts/accountsstatement/{accountId}
```

Returns account details and all operations.

#### Watch Account Operations (Server-Sent Events)
```shell script
GET /query/accounts/watch/{accountId}
Accept: text/event-stream
```

Streams real-time operations for the specified account.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot 3.4.2** | Application framework |
| **Axon Framework 4.10.3** | Event Sourcing & CQRS implementation |
| **Spring Data JPA** | Read model persistence |
| **PostgreSQL** | Read model database |
| **Axon Server** | Event Store & message routing |
| **Lombok** | Boilerplate reduction |
| **Project Reactor** | Reactive streams for SSE |
| **SpringDoc OpenAPI** | API documentation |
| **Maven** | Build tool |

---

## 🐳 Docker Setup

The `docker-compose.yml` provides:

### Axon Server
- **Image:** `axoniq/axonserver:latest`
- **Ports:** 8024 (HTTP), 8124 (gRPC)
- **Purpose:** Event Store, Command Bus, Query Bus

### PostgreSQL
- **Image:** `postgres:latest`
- **Port:** 5432
- **Database:** `event_sourcing_db`
- **Credentials:** admin / 1234

### pgAdmin
- **Image:** `dpage/pgadmin4`
- **Port:** 8088
- **Credentials:** med@gmail.com / azer

---

## 📊 Business Rules

The application enforces the following rules:

1. ✅ Accounts must be created with a positive initial balance
2. ✅ Only **ACTIVATED** accounts can be credited or debited
3. ✅ Debits cannot exceed the current balance
4. ✅ Account status cannot be updated to its current status
5. ✅ All operations are recorded as immutable events

---

## 🎓 Learning Resources

To better understand the patterns used:

- [Axon Framework Documentation](https://docs.axoniq.io/)
- [Event Sourcing Pattern (Martin Fowler)](https://martinfowler.com/eaaDev/EventSourcing.html)
- [CQRS Pattern (Microsoft)](https://docs.microsoft.com/en-us/azure/architecture/patterns/cqrs)

---

## 📝 License

This project is provided as-is for educational purposes. Add a `LICENSE` file to specify reuse terms.

---

**Happy Event Sourcing! 🚀**