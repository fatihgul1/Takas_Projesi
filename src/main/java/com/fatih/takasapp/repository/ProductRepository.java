package com.fatih.takasapp.repository;
import com.fatih.takasapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
}