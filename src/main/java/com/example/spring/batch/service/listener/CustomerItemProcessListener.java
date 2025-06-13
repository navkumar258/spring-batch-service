package com.example.spring.batch.service.listener;

import com.example.spring.batch.service.model.Customer;
import com.example.spring.batch.service.model.CustomerCsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemProcessListener implements ItemProcessListener<CustomerCsvRow, Customer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerItemProcessListener.class);
  private long processedCount = 0;
  private long filteredCount = 0;

  @Override
  public void afterProcess(CustomerCsvRow item, Customer result) {
    if (result != null) {
      processedCount++;
    } else {
      filteredCount++; // Item was filtered out by returning null
    }
  }

  @Override
  public void onProcessError(CustomerCsvRow item, Exception e) {
    LOGGER.error("Error processing item: {} - {}", item, e.getMessage());
  }

  public long getProcessedCount() {
    return processedCount;
  }

  public long getFilteredCount() {
    return filteredCount;
  }
}
