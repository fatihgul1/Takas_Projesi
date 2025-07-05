package com.fatih.takasapp.repository;

import com.fatih.takasapp.entity.Exchange;
import com.fatih.takasapp.entity.ExchangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    List<Exchange> findBySellerEmail(String email);
    List<Exchange> findByBuyerEmail(String email);
    List<Exchange> findByTargetProductIdAndStatus(Long productId, ExchangeStatus status);
}