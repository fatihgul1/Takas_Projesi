package com.fatih.takasapp.controller;

import com.fatih.takasapp.entity.Exchange;
import com.fatih.takasapp.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exchanges")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    // Tüm takas tekliflerini getir
    @GetMapping
    public List<Exchange> getAllExchanges() {
        return exchangeService.getAllExchanges();
    }

    @PostMapping("/create")
    public Exchange createExchange(@RequestParam Long buyerId, @RequestParam Long sellerId, @RequestParam Long productId) {
        return exchangeService.createExchange(buyerId, sellerId, productId);
    }

    @PutMapping("/approve/{exchangeId}")
    public Optional<Exchange> approveExchange(@PathVariable Long exchangeId) {
        return exchangeService.approveExchange(exchangeId);
    }

    @PutMapping("/complete/{exchangeId}")
    public Optional<Exchange> completeExchange(@PathVariable Long exchangeId) {
        return exchangeService.completeExchange(exchangeId);
    }

    @PutMapping("/cancel/{exchangeId}")
    public Optional<Exchange> cancelExchange(@PathVariable Long exchangeId) {
        return exchangeService.cancelExchange(exchangeId);
    }
}
