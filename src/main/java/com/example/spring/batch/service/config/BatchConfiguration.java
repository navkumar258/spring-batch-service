package com.example.spring.batch.service.config;

import com.example.spring.batch.service.listener.*;
import com.example.spring.batch.service.model.Customer;
import com.example.spring.batch.service.model.CustomerCsvRow;
import com.example.spring.batch.service.service.CustomerProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final DataSource dataSource;

  private final CustomerJobExecutionListener jobExecutionListener;
  private final CustomerStepExecutionListener stepExecutionListener;
  private final CustomerItemReadListener itemReadListener;
  private final CustomerItemProcessListener itemProcessListener;
  private final CustomerItemWriteListener itemWriteListener;

  public BatchConfiguration(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            DataSource dataSource,
                            CustomerJobExecutionListener jobExecutionListener,
                            CustomerStepExecutionListener stepExecutionListener,
                            CustomerItemReadListener itemReadListener,
                            CustomerItemProcessListener itemProcessListener,
                            CustomerItemWriteListener itemWriteListener) {
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
    this.dataSource = dataSource;
    this.jobExecutionListener = jobExecutionListener;
    this.stepExecutionListener = stepExecutionListener;
    this.itemReadListener = itemReadListener;
    this.itemProcessListener = itemProcessListener;
    this.itemWriteListener = itemWriteListener;
  }

  // 1. ItemReader: Reads from CSV
  @Bean
  public FlatFileItemReader<CustomerCsvRow> customerItemReader() {
    FlatFileItemReader<CustomerCsvRow> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("customers.csv")); // Input CSV file
    reader.setLineMapper(customerLineMapper());
    return reader;
  }

  private LineMapper<CustomerCsvRow> customerLineMapper() {
    DefaultLineMapper<CustomerCsvRow> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setNames("id", "firstName", "lastName", "email", "phone", "loyaltyTier"); // CSV header
    lineTokenizer.setDelimiter(",");
    lineMapper.setLineTokenizer(lineTokenizer);

    RecordFieldSetMapper<CustomerCsvRow> fieldSetMapper = new RecordFieldSetMapper<>(CustomerCsvRow.class);
    lineMapper.setFieldSetMapper(fieldSetMapper);

    return lineMapper;
  }

  // 2. ItemProcessor: Transforms and Validates
  @Bean
  public ItemProcessor<CustomerCsvRow, Customer> customerItemProcessor() {
    return new CustomerProcessor(); // Custom processor logic
  }

  // 3. ItemWriter: Writes to Database
  @Bean
  public JdbcBatchItemWriter<Customer> customerItemWriter() {
    JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    writer.setDataSource(dataSource);
    writer.setSql("INSERT INTO customers (id, first_name, last_name, email, phone, new_loyalty_tier) " +
            "VALUES (:id, :firstName, :lastName, :email, :phone, :newLoyaltyTier)");
    return writer;
  }

  // Step: Defines the chunk-oriented processing (read-process-write)
  @Bean
  public Step customerMigrationStep(
          ItemReader<CustomerCsvRow> customerItemReader,
          ItemProcessor<CustomerCsvRow, Customer> customerItemProcessor,
          ItemWriter<Customer> customerItemWriter) {
    return new StepBuilder("customerMigrationStep", jobRepository)
            .<CustomerCsvRow, Customer>chunk(500, transactionManager) // Process 1000 records at a time in a transaction
            .reader(customerItemReader)
            .listener(itemReadListener)
            .processor(customerItemProcessor)
            .listener(itemProcessListener)
            .writer(customerItemWriter)
            .listener(itemWriteListener)
            .faultTolerant() // Enable fault tolerance
            .skipLimit(100) // Skip up to 10 bad records
            .skip(Exception.class) // Skip any exception
            .listener(stepExecutionListener)
            .build();
  }

  // Job: Orchestrates the steps
  @Bean
  public Job importCustomerJob(Step customerMigrationStep) {
    return new JobBuilder("importCustomerJob", jobRepository)
            .start(customerMigrationStep)
            .listener(jobExecutionListener)
            .build();
  }
}
