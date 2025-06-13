package com.example.spring.batch.service.model;

public record Customer(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String oldLoyaltyTier,
        String newLoyaltyTier // Transformed field
) {}
