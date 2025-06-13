package com.example.spring.batch.service.service;

import com.example.spring.batch.service.model.Customer;
import com.example.spring.batch.service.model.CustomerCsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor implements ItemProcessor<CustomerCsvRow, Customer> {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProcessor.class);

  @Override
  public Customer process(CustomerCsvRow csvRow) throws Exception {
    // Simple validation and transformation examples
    if (csvRow.email() == null || !csvRow.email().contains("@")) {
      // Or throw an exception to be caught by fault tolerance
      LOGGER.warn("Skipping invalid email: {}", csvRow.email());
      return null; // Return null to filter out this item
    }

    // Example for testing filtering: filter out customers with "UnknownTier" from the CSV
    if ("UnknownTier".equalsIgnoreCase(csvRow.loyaltyTier())) {
      LOGGER.warn("Filtering out customer with unknown loyalty tier: {}", csvRow.loyaltyTier());
      return null;
    }

    String formattedPhone = formatPhoneNumber(csvRow.phone());
    String newLoyaltyTier = mapLoyaltyTier(csvRow.loyaltyTier());

    return new Customer(
            Long.valueOf(csvRow.id()),
            csvRow.firstName(),
            csvRow.lastName(),
            csvRow.email(),
            formattedPhone,
            csvRow.loyaltyTier(),
            newLoyaltyTier
    );
  }

  private String formatPhoneNumber(String phone) {
    // Example: Remove non-digits
    return phone != null ? phone.replaceAll("\\D", "") : null;
  }

  private String mapLoyaltyTier(String oldTier) {
    return switch (oldTier) {
      case "Gold" -> "Premium";
      case "Silver" -> "Standard";
      case "Bronze" -> "Basic";
      case "Platinum" -> "Elite";
      default -> "Unknown";
    };
  }
}
