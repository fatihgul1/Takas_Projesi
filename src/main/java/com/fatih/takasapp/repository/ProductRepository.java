package com.fatih.takasapp.repository;

import com.fatih.takasapp.entity.Product;
import com.fatih.takasapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwner(User owner);
}