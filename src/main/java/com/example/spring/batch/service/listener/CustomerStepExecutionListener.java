package com.example.spring.batch.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerStepExecutionListener implements StepExecutionListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerStepExecutionListener.class);
  private final CustomerItemReadListener readListener;
  private final CustomerItemProcessListener processListener;
  private final CustomerItemWriteListener writeListener;

  // Inject our item listeners to get their counts
  public CustomerStepExecutionListener(
          CustomerItemReadListener readListener,
          CustomerItemProcessListener processListener,
          CustomerItemWriteListener writeListener) {
    this.readListener = readListener;
    this.processListener = processListener;
    this.writeListener = writeListener;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    LOGGER.info("Step '{}' started.", stepExecution.getStepName());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    LOGGER.info("Step '{}' finished with status: {}", stepExecution.getStepName(), stepExecution.getStatus());
    LOGGER.info("  Read Count: {}", readListener.getReadCount());
    LOGGER.info("  Processed Count: {}", processListener.getProcessedCount());
    LOGGER.info("  Filtered Count: {}", processListener.getFilteredCount());
    LOGGER.info("  Written Count: {}", writeListener.getWriteCount());
    LOGGER.info("  Write Skipped Count (due to errors): {}", writeListener.getWriteSkipCount());
    LOGGER.info("  Commit Count: {}", stepExecution.getCommitCount());
    LOGGER.info("  Rollback Count: {}", stepExecution.getRollbackCount());
    LOGGER.info("  Skip Count (from fault tolerance, e.g., bad record): {}", stepExecution.getSkipCount());
    LOGGER.info("  Read Skip Count: {}", stepExecution.getReadSkipCount());
    LOGGER.info("  Process Skip Count: {}", stepExecution.getProcessSkipCount());
    LOGGER.info("  Write Skip Count (item-level): {}", stepExecution.getWriteSkipCount());


    return stepExecution.getExitStatus();
  }
}