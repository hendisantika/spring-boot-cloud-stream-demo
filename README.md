# Spring Boot Cloud Stream Demo

A comprehensive demonstration of Spring Cloud Stream with Apache Kafka, showcasing real-time stream processing with a
Producer-Processor-Consumer pattern.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [Monitoring](#monitoring)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

## Overview

This project demonstrates real-time stream processing using Spring Cloud Stream with Apache Kafka. The application
consists of three functional components working together in a pipeline:

**Producer** → `numbers` topic → **Processor** → `squares` topic → **Consumer**

### Components

1. **Producer**: Generates sequential numbers (0, 1, 2, 3, ...) every second
    - In real-world scenarios: User actions, sensor data, transaction logs, etc.

2. **Processor**: Consumes numbers, squares them, and publishes to another topic
    - In real-world scenarios: Data transformation, enrichment, filtering, aggregation, etc.

3. **Consumer**: Receives processed data and displays it
    - In real-world scenarios: Data storage, notifications, dashboards, etc.

## Architecture

```
┌──────────┐         ┌──────────┐         ┌──────────┐
│          │         │          │         │          │
│ Producer ├────────►│ Processor├────────►│ Consumer │
│          │         │          │         │          │
└──────────┘         └──────────┘         └──────────┘
      │                    │                    │
      │                    │                    │
      ▼                    ▼                    ▼
   [numbers topic]   [numbers → squares]  [squares topic]
   (0,1,2,3...)     (0,1,4,9,16...)      (prints results)
```

### Kafka Topics

- **numbers**: Raw data from producer (sequential numbers)
- **squares**: Processed data (squared numbers)

## Technologies

- **Java 21**: Programming language
- **Spring Boot 3.5.6**: Application framework
- **Spring Cloud Stream 2025.0.0**: Stream processing abstraction
- **Apache Kafka**: Message broker
- **Spring WebFlux**: Reactive web framework
- **Project Reactor**: Reactive programming
- **Docker & Docker Compose**: Containerization
- **Kafdrop**: Kafka UI for monitoring

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or later ([Download](https://adoptium.net/))
  ```bash
  java -version
  ```

- **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop))
  ```bash
  docker --version
  docker-compose --version
  ```

- **Maven 3.9+** (included via Maven Wrapper)
  ```bash
  ./mvnw --version
  ```

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd spring-boot-cloud-stream-demo
```

### 2. Start Docker Desktop

Ensure Docker Desktop is running on your machine:

- **macOS**: Open Docker Desktop from Applications
- **Windows**: Open Docker Desktop from Start Menu
- **Linux**: Start Docker daemon
  ```bash
  sudo systemctl start docker
  ```

Verify Docker is running:

```bash
docker info
```

### 3. Start Kafka Infrastructure

Launch Zookeeper, Kafka, and Kafdrop using Docker Compose:

```bash
docker-compose up -d
```

This will start:

- **Zookeeper** on port `2181`
- **Kafka** on port `9092`
- **Kafdrop** (Kafka UI) on port `9000`

Verify all services are healthy:

```bash
docker-compose ps
```

You should see all services with status "healthy" or "running".

### 4. Build the Application

```bash
./mvnw clean package
```

Or skip tests for faster build:

```bash
./mvnw clean package -DskipTests
```

## Running the Application

### Option 1: Using Maven

```bash
./mvnw spring-boot:run
```

### Option 2: Using JAR

```bash
java -jar target/cloud-stream-demo-0.0.1-SNAPSHOT.jar
```

## Verifying the Application

Once the application is running, you should see output similar to:

```
Producer → Flux.interval: 0
Processor → Received: 0
Processor → Sending: 0
Consumer Received : 0

Producer → Flux.interval: 1
Processor → Received: 1
Processor → Sending: 1
Consumer Received : 1

Producer → Flux.interval: 2
Processor → Received: 2
Processor → Sending: 4
Consumer Received : 4

Producer → Flux.interval: 3
Processor → Received: 3
Processor → Sending: 9
Consumer Received : 9
```

The flow demonstrates:

1. Producer generates: `0, 1, 2, 3, 4, 5...`
2. Processor squares: `0, 1, 4, 9, 16, 25...`
3. Consumer receives and prints the squared values

## Monitoring

### Kafdrop - Kafka Web UI

Access Kafdrop for visual monitoring of Kafka topics and messages:

```
http://localhost:9000
```

Features:

- View all topics (`numbers`, `squares`)
- Inspect messages in real-time
- Monitor consumer groups
- Check topic configurations
- View broker information

### Application Logs

Monitor application logs for reactive stream processing:

```bash
# Follow logs
./mvnw spring-boot:run

# Or with JAR
java -jar target/cloud-stream-demo-0.0.1-SNAPSHOT.jar
```

## Testing

Run the test suite:

```bash
./mvnw test
```

Run specific tests:

```bash
./mvnw test -Dtest=SpringBootCloudStreamDemoApplicationTests
```

## Configuration

The application configuration is in `src/main/resources/application.yml`:

```yaml
spring.application.name: spring-boot-cloud-stream-demo
spring.cloud.stream:
  function:
    definition: producer;processor;consumer
  bindings:
    producer-out-0:
      destination: numbers
    processor-in-0:
      destination: numbers
    processor-out-0:
      destination: squares
    consumer-in-0:
      destination: squares
```

### Key Configuration Points

- **Function Definition**: Defines the three beans (`producer`, `processor`, `consumer`)
- **Bindings**: Maps functions to Kafka topics
    - `producer-out-0` → publishes to `numbers` topic
    - `processor-in-0` → subscribes to `numbers` topic
    - `processor-out-0` → publishes to `squares` topic
    - `consumer-in-0` → subscribes to `squares` topic

## Troubleshooting

### Docker Daemon Not Running

**Error**: `Cannot connect to the Docker daemon`

**Solution**: Start Docker Desktop or Docker daemon

```bash
# macOS/Windows: Open Docker Desktop
# Linux:
sudo systemctl start docker
```

### Port Already in Use

**Error**: `Bind for 0.0.0.0:9092 failed: port is already allocated`

**Solution**: Stop the conflicting service or change ports in `docker-compose.yml`

```bash
# Find process using port
lsof -i :9092
# Kill process
kill -9 <PID>
```

### Application Fails to Connect to Kafka

**Error**: `Connection to node -1 could not be established`

**Solution**:

1. Ensure Kafka is running: `docker-compose ps`
2. Check Kafka logs: `docker-compose logs kafka`
3. Restart Kafka: `docker-compose restart kafka`

### No Messages Flowing

**Checklist**:

1. Verify Kafka is healthy: `docker-compose ps`
2. Check topics exist in Kafdrop: `http://localhost:9000`
3. Review application logs for errors
4. Restart the application

### Clean Restart

To completely reset the environment:

```bash
# Stop and remove containers, networks, volumes
docker-compose down -v

# Start fresh
docker-compose up -d

# Rebuild and restart application
./mvnw clean package
./mvnw spring-boot:run
```

## Project Structure

```
spring-boot-cloud-stream-demo/
├── src/
│   ├── main/
│   │   ├── java/id/my/hendisantika/cloudstreamdemo/
│   │   │   └── SpringBootCloudStreamDemoApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/id/my/hendisantika/cloudstreamdemo/
│           └── SpringBootCloudStreamDemoApplicationTests.java
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Stopping the Application

### Stop Spring Boot Application

Press `Ctrl+C` in the terminal running the application

### Stop Kafka Infrastructure

```bash
# Stop containers
docker-compose stop

# Stop and remove containers
docker-compose down

# Stop and remove containers, networks, and volumes
docker-compose down -v
```

## Additional Resources

- [Spring Cloud Stream Documentation](https://spring.io/projects/spring-cloud-stream)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Project Reactor Documentation](https://projectreactor.io/docs)

## License

This project is available for educational and demonstration purposes.

## Contributing

Feel free to submit issues and enhancement requests!

## Author

Hendi Santika

---

**Happy Streaming!**
