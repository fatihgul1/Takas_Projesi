package com.fatih.takasapp.service;

import com.fatih.takasapp.entity.*;
import com.fatih.takasapp.repository.OrderRepository;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Sipariş oluştur
    public Order createOrder(Long buyerId, Long productId) {
        Optional<User> buyerOpt = userRepository.findById(buyerId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (buyerOpt.isEmpty() || productOpt.isEmpty()) {
            throw new IllegalArgumentException("Alıcı veya ürün bulunamadı");
        }

        Order order = new Order();
        order.setBuyer(buyerOpt.get());
        order.setProduct(productOpt.get());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    // Ödemeyi tamamla (gerçek ödeme entegrasyonu yerine direkt onaylıyoruz)
    public Optional<Order> completeOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        orderOpt.ifPresent(order -> order.setStatus(OrderStatus.COMPLETED));
        return orderOpt.map(orderRepository::save);
    }

    // Siparişi iptal et
    public Optional<Order> cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        orderOpt.ifPresent(order -> order.setStatus(OrderStatus.CANCELLED));
        return orderOpt.map(orderRepository::save);
    }
}
