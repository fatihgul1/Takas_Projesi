package com.fatih.takasapp.service;

import com.fatih.takasapp.entity.Exchange;
import com.fatih.takasapp.entity.ExchangeStatus;
import com.fatih.takasapp.entity.Product;
import com.fatih.takasapp.entity.User;
import com.fatih.takasapp.repository.ExchangeRepository;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ExchangeService(ExchangeRepository exchangeRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.exchangeRepository = exchangeRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Exchange createExchange(Long buyerId, Long sellerId, Long productId) {
        User buyer = userRepository.findById(buyerId).orElseThrow(() -> new IllegalArgumentException("Alıcı bulunamadı!"));
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new IllegalArgumentException("Satıcı bulunamadı!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı!"));

        Exchange exchange = new Exchange();
        exchange.setBuyer(buyer);
        exchange.setSeller(seller);
        exchange.setProduct(product);

        return exchangeRepository.save(exchange);
    }

    public Optional<Exchange> approveExchange(Long exchangeId) {
        return updateExchangeStatus(exchangeId, ExchangeStatus.APPROVED);
    }

    public Optional<Exchange> completeExchange(Long exchangeId) {
        return updateExchangeStatus(exchangeId, ExchangeStatus.COMPLETED);
    }

    public Optional<Exchange> cancelExchange(Long exchangeId) {
        return updateExchangeStatus(exchangeId, ExchangeStatus.CANCELLED);
    }

    private Optional<Exchange> updateExchangeStatus(Long exchangeId, ExchangeStatus status) {
        return exchangeRepository.findById(exchangeId).map(exchange -> {
            exchange.setStatus(status);
            return exchangeRepository.save(exchange);
        });
    }
    public List<Exchange> getAllExchanges() {
        return exchangeRepository.findAll();
    }
}