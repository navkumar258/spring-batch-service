package com.example.spring.batch.service.model;

public record CustomerCsvRow(
        String id, // From CSV, might be String
        String firstName,
        String lastName,
        String email,
        String phone,
        String loyaltyTier
) {}
