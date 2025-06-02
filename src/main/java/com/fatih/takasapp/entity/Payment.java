package com.fatih.takasapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private Double amount;
    private String paymentMethod; // Örneğin, "Credit Card", "PayPal"

    // Getter ve Setter metotları
    // ...existing code...
}
