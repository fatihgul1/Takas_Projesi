package com.fatih.takasapp.controller;

import com.fatih.takasapp.entity.Order;
import com.fatih.takasapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Sipariş oluştur
    @PostMapping("/create")
    public Order createOrder(@RequestParam Long buyerId, @RequestParam Long productId) {
        return orderService.createOrder(buyerId, productId);
    }

    // Siparişi tamamla (ödeme tamamlandı gibi varsay)
    @PutMapping("/complete/{orderId}")
    public Optional<Order> completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    // Siparişi iptal et
    @PutMapping("/cancel/{orderId}")
    public Optional<Order> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
