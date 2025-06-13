package com.example.spring.batch.service.listener;

import com.example.spring.batch.service.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemWriteListener implements ItemWriteListener<Customer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerItemWriteListener.class);
  private long writeCount = 0;
  private long writeSkipCount = 0; // Items that failed to write

  @Override
  public void afterWrite(Chunk<? extends Customer> items) {
    writeCount += items.size();
  }

  @Override
  public void onWriteError(Exception exception, Chunk<? extends Customer> items) {
    LOGGER.error("Error writing chunk: {}", exception.getMessage());
    writeSkipCount += items.size(); // Count the entire chunk as skipped if there's a write error
    // For more granular skips, configure skip policy on ItemWriter.
    items.forEach(item -> LOGGER.error("  Problematic item: {}", item));
  }

  public long getWriteCount() {
    return writeCount;
  }

  public long getWriteSkipCount() {
    return writeSkipCount;
  }
}
