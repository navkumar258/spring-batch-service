package com.example.spring.batch.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemReadListener implements ItemReadListener<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerItemReadListener.class);
  private long readCount = 0;

  @Override
  public void afterRead(Object item) {
    readCount++;
  }

  @Override
  public void onReadError(Exception ex) {
    LOGGER.error("Error reading item: {}", ex.getMessage());
  }

  public long getReadCount() {
    return readCount;
  }
}
