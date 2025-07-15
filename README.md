# Spring Batch Service

A Spring Batch application demonstrating CSV processing with database persistence and Kafka event publishing using Java 21 and Spring Batch 5.x.

## 🚀 Features

- **CSV Processing**: Read and parse CSV files with configurable delimiters and headers
- **Database Integration**: Write processed data to various database systems (MySQL, PostgreSQL, etc.)
- **Kafka Integration**: Publish events to Kafka topics for real-time processing
- **Batch Processing**: Efficient processing of large datasets with chunk-based processing
- **Error Handling**: Comprehensive error handling with retry mechanisms
- **Monitoring**: Job execution monitoring and metrics
- **Configurable**: Flexible configuration for different data formats and destinations

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.8+** or **Gradle 8+**
- **Database**: MySQL 8.0+, PostgreSQL 13+, or H2 (for testing)
- **Apache Kafka** 3.0+ (optional, for event publishing)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

## 🛠️ Technology Stack

- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.x** - Application framework
- **Spring Batch 5.x** - Batch processing framework
- **Spring Data JPA** - Database operations
- **Apache Kafka** - Event streaming
- **Maven/Gradle** - Build tools
- **Docker** - Containerization (optional)

##️ Installation

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/spring-batch-service.git
cd spring-batch-service
```

### 2. Configure Database
Update `application.yml` with your database configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/batch_service
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

### 3. Configure Kafka (Optional)
If using Kafka, update the configuration:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### 4. Build the Application
```bash
# Using Maven
mvn clean install

# Using Gradle
./gradlew build
```

## 🚀 Running the Application

### Local Development
```bash
# Run with Maven
mvn spring-boot:run

# Run with Gradle
./gradlew bootRun

# Run the JAR file
java -jar target/spring-batch-service-1.0.0.jar
```

### Docker (Optional)
```bash
# Build Docker image
docker build -t spring-batch-service .

# Run container
docker run -p 8080:8080 spring-batch-service
```

## 📁 Project Structure

The project follows a standard Spring Boot structure with batch-specific components:

src/
├── main/
│ ├── java/
│ │ └── com/batch-service/batch/
│ │ ├── config/
│ │ │ ├── BatchConfig.java # Batch job configuration
│ │ │ ├── DatabaseConfig.java # Database configuration
│ │ │ └── KafkaConfig.java # Kafka configuration
│ │ ├── model/
│ │ │ └── DataRecord.java # Data model/entity
│ │ ├── processor/
│ │ │ └── DataProcessor.java # Business logic processor
│ │ ├── reader/
│ │ │ └── CustomCsvReader.java # CSV file reader
│ │ ├── writer/
│ │ │ ├── DatabaseWriter.java # Database writer
│ │ │ └── KafkaWriter.java # Kafka event writer
│ │ └── SpringBatchbatch-serviceApplication.java
│ └── resources/
│ ├── application.yml # Application configuration
│ ├── data/
│ │ └── sample.csv # Sample input data
│ └── schema.sql # Database schema
└── test/
└── java/
└── com/example/batch/
└── BatchJobTest.java # Integration tests

**Key Components:**
- **Config**: Contains all configuration classes for batch, database, and Kafka
- **Model**: Data entities and DTOs
- **Processor**: Business logic for data transformation
- **Reader**: Custom CSV reading implementations
- **Writer**: Database and Kafka writing implementations

## ⚙️ Configuration

### Application Properties

```yaml
# Batch Configuration
spring:
  batch:
    job:
      enabled: false  # Disable auto-start
    jdbc:
      initialize-schema: always
    
# Job Parameters
job:
  input-file: classpath:data/input.csv
  chunk-size: 1000
  retry-limit: 3
  
# Kafka Configuration
kafka:
  topic:
    name: batch-events
    partitions: 3
    replicas: 1
```

### Job Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `input.file` | Input CSV file path | `classpath:data/input.csv` |
| `chunk.size` | Processing chunk size | `1000` |
| `retry.limit` | Number of retry attempts | `3` |
| `output.destination` | Output destination (db/kafka) | `db` |

## 🎯 Usage Examples

### 1. Basic CSV to Database Processing

```bash
# Run job with parameters
java -jar spring-batch-service.jar \
  --spring.batch.job.names=csvToDatabaseJob \
  --input.file=classpath:data/input.csv \
  --chunk.size=500
```

### 2. CSV to Kafka Processing

```bash
# Run job with Kafka output
java -jar spring-batch-service.jar \
  --spring.batch.job.names=csvToKafkaJob \
  --input.file=classpath:data/input.csv \
  --output.destination=kafka
```

### 3. Programmatic Job Execution

```java
@Autowired
private JobLauncher jobLauncher;

@Autowired
private Job csvToDatabaseJob;

public void runJob() throws Exception {
    JobParameters params = new JobParametersBuilder()
        .addString("input.file", "classpath:data/input.csv")
        .addLong("timestamp", System.currentTimeMillis())
        .toJobParameters();
    
    jobLauncher.run(csvToDatabaseJob, params);
}
```

## 📊 Monitoring and Metrics

### Job Execution Monitoring

Access job execution details via REST endpoints:

```bash
# Get all job executions
curl http://localhost:8080/actuator/batch/jobs

# Get specific job execution
curl http://localhost:8080/actuator/batch/jobs/{executionId}

# Get job execution metrics
curl http://localhost:8080/actuator/metrics/batch.jobs
```

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database health
curl http://localhost:8080/actuator/health/db

# Kafka health
curl http://localhost:8080/actuator/health/kafka
```

## 🧪 Testing

### Unit Tests
```bash
# Run unit tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Integration Tests
```bash
# Run integration tests
mvn verify -P integration-test
```

### Test Data
Sample CSV format for testing:

```csv
id,name,email,age,city
1,John Doe,john@example.com,30,New York
2,Jane Smith,jane@example.com,25,Los Angeles
3,Bob Johnson,bob@example.com,35,Chicago
```

## 🛠️ Customization

### Custom Data Model

```java
@Entity
public class CustomDataRecord {
    @Id
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String city;
    
    // Getters, setters, constructors
}
```

### Custom Processor

```java
@Component
public class CustomDataProcessor implements ItemProcessor<InputRecord, OutputRecord> {
    
    @Override
    public OutputRecord process(InputRecord item) throws Exception {
        // Custom processing logic
        return new OutputRecord(item);
    }
}
```

### Custom Reader/Writer

```java
@Component
public class CustomCsvReader extends FlatFileItemReader<DataRecord> {
    // Custom reading logic
}

@Component
public class CustomDatabaseWriter extends JpaItemWriter<DataRecord> {
    // Custom writing logic
}
```

## 🐛 Troubleshooting

### Common Issues

**Job not starting:**
- Check if `spring.batch.job.enabled=false` in application.yml
- Verify job parameters are correct
- Check database connectivity

**CSV parsing errors:**
- Verify CSV format and delimiter
- Check for encoding issues (UTF-8 recommended)
- Validate header mapping

**Database connection issues:**
- Verify database credentials
- Check database server is running
- Ensure proper JDBC driver

**Kafka connection issues:**
- Verify Kafka broker is running
- Check topic exists and has proper permissions
- Validate serializer configuration

### Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.example.batch: DEBUG
    org.springframework.batch: DEBUG
```

## 📈 Performance Optimization

### Chunk Size Tuning
```yaml
job:
  chunk-size: 1000  # Adjust based on memory and performance
```

### Database Optimization
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 50
        order_inserts: true
        order_updates: true
```

### Memory Management
```bash
# JVM options for large datasets
java -Xmx4g -Xms2g -jar spring-batch-service.jar
```

## 🔒 Security

### Database Security
- Use connection pooling
- Implement proper authentication
- Use encrypted connections (SSL/TLS)

### Kafka Security
- Enable SASL authentication
- Use SSL/TLS encryption
- Implement proper ACLs

## 📚 API Documentation

### REST Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/jobs` | GET | List all jobs |
| `/api/jobs/{jobName}/executions` | GET | Get job executions |
| `/api/jobs/{jobName}/start` | POST | Start a job |
| `/api/jobs/{executionId}/stop` | POST | Stop a job execution |

### Example API Usage

```bash
# Start a job
curl -X POST http://localhost:8080/api/jobs/csvToDatabaseJob/start \
  -H "Content-Type: application/json" \
  -d '{"inputFile": "classpath:data/input.csv"}'

# Get job status
curl http://localhost:8080/api/jobs/csvToDatabaseJob/executions
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup
```bash
# Clone and setup
git clone https://github.com/yourusername/spring-batch-service.git
cd spring-batch-service

# Install dependencies
mvn clean install

# Run tests
mvn test

# Start development server
mvn spring-boot:run
```

## ��️ Architecture

The application follows a layered architecture pattern with clear separation of concerns:

**Data Flow:**
1. **CSV Input** → FlatFileItemReader reads CSV files
2. **Processing** → ItemProcessor applies business logic
3. **Output** → ItemWriter persists to database or publishes to Kafka
4. **Metadata** → JobRepository tracks execution status
5. **Monitoring** → Actuator endpoints provide metrics

**Key Components:**
- **Job**: Defines the overall batch process
- **Step**: Individual processing unit within a job
- **Chunk**: Configurable batch size for processing
- **Reader**: Extracts data from CSV source
- **Processor**: Transforms and validates data
- **Writer**: Persists data to target systems
- **Repository**: Stores job execution metadata

**Error Handling:**
- Retry mechanism for transient failures
- Skip policy for invalid records
- Comprehensive logging and monitoring

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/spring-batch-service/issues)
- **Documentation**: [Wiki](https://github.com/yourusername/spring-batch-service/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/spring-batch-service/discussions)

## Acknowledgments

- [Spring Batch](https://spring.io/projects/spring-batch) - The batch processing framework
- [Spring Boot](https://spring.io/projects/spring-boot) - The application framework
- [Apache Kafka](https://kafka.apache.org/) - The event streaming platform

## 📈 Version History

### v1.0.0
- Initial release
- CSV to database processing
- CSV to Kafka processing
- Basic monitoring and metrics
- Comprehensive error handling

---

**⭐ If this project helps you, please consider giving it a star!**

For more information about Spring Batch, visit the [official documentation](https://docs.spring.io/spring-batch/docs/current/reference/html/).
