package com.example.spring.batch.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomerJobExecutionListener implements JobExecutionListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerJobExecutionListener.class);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final String LOG_SEPARATOR = "---------------------------------------------------";

  @Override
  public void beforeJob(JobExecution jobExecution) {
    LOGGER.info(LOG_SEPARATOR);
    LOGGER.info("JOB STARTED: {} at {}", jobExecution.getJobInstance().getJobName(),
            LocalDateTime.now().format(FORMATTER));
    LOGGER.info("Job Parameters: {}", jobExecution.getJobParameters());
    LOGGER.info(LOG_SEPARATOR);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    LOGGER.info(LOG_SEPARATOR);
    LOGGER.info("JOB FINISHED: {} with status {} at {}", jobExecution.getJobInstance().getJobName(),
            jobExecution.getStatus(),
            LocalDateTime.now().format(FORMATTER));
    LOGGER.info("Exit Status: {}", jobExecution.getExitStatus().getExitCode());
    LOGGER.info(
            "Duration: {}ms",
            Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis()
    );
    LOGGER.info(LOG_SEPARATOR);
  }
}
