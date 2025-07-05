package com.fatih.takasapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product offeredProduct;

    @ManyToOne
    private Product targetProduct;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;
}
