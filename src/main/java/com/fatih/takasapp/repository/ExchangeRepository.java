package com.fatih.takasapp.repository;

import com.fatih.takasapp.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
}