package com.fatih.takasapp.service;

import com.fatih.takasapp.dto.ExchangeDTO;
import com.fatih.takasapp.dto.ExchangeRequestDto;
import com.fatih.takasapp.entity.*;
import com.fatih.takasapp.repository.ExchangeRepository;
import com.fatih.takasapp.repository.ProductRepository;
import com.fatih.takasapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Exchange createExchange(Long offeredProductId, Long targetProductId, String buyerEmail) {
        Product offered = productRepository.findById(offeredProductId)
                .orElseThrow(() -> new RuntimeException("Teklif edilen ürün bulunamadı"));

        Product target = productRepository.findById(targetProductId)
                .orElseThrow(() -> new RuntimeException("Hedef ürün bulunamadı"));

        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        User seller = target.getOwner();

        Exchange exchange = new Exchange();
        exchange.setOfferedProduct(offered);
        exchange.setTargetProduct(target);
        exchange.setBuyer(buyer);
        exchange.setSeller(seller);
        exchange.setStatus(ExchangeStatus.PENDING);

        return exchangeRepository.save(exchange);
    }

    public List<ExchangeDTO> getIncomingExchanges(String email) {
        return exchangeRepository.findBySellerEmail(email)
                .stream()
                .map(ex -> convertToDto(ex, email))
                .collect(Collectors.toList());
    }

    public List<ExchangeDTO> getOutgoingExchanges(String email) {
        return exchangeRepository.findByBuyerEmail(email)
                .stream()
                .map(ex -> convertToDto(ex, email))
                .collect(Collectors.toList());
    }

    public void updateStatus(Long id, ExchangeStatus status) {
        Exchange exchange = exchangeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Takas teklifi bulunamadı"));

        if (status == ExchangeStatus.ACCEPTED) {
            // Kabul edilen ürün için diğer tüm PENDING teklifleri reddet
            List<Exchange> pendingOffers = exchangeRepository.findByTargetProductIdAndStatus(
                    exchange.getTargetProduct().getId(), ExchangeStatus.PENDING);

            for (Exchange e : pendingOffers) {
                if (!e.getId().equals(id)) {
                    e.setStatus(ExchangeStatus.REJECTED);
                    exchangeRepository.save(e);
                }
            }
            // 👇 İlanı yayından kaldır
            Product target = exchange.getTargetProduct();
            target.setActive(false);
            productRepository.save(target);
        }

        exchange.setStatus(status);
        exchangeRepository.save(exchange);
    }

    private ExchangeDTO convertToDto(Exchange ex, String currentUserEmail) {
        ExchangeDTO dto = new ExchangeDTO();
        dto.setId(ex.getId());
        dto.setStatus(ex.getStatus());
        dto.setOfferedProductName(ex.getOfferedProduct().getName());
        dto.setTargetProductName(ex.getTargetProduct().getName());
        dto.setBuyer(ex.getBuyer());
        dto.setSeller(ex.getSeller());

        if (ex.getStatus() == ExchangeStatus.ACCEPTED) {
            if (ex.getBuyer().getEmail().equals(currentUserEmail)) {
                dto.setContactPhone(ex.getSeller().getPhoneNumber());
            } else if (ex.getSeller().getEmail().equals(currentUserEmail)) {
                dto.setContactPhone(ex.getBuyer().getPhoneNumber());
            }
        }
        return dto;
    }

    public Optional<ExchangeDTO> getOfferForTarget(Long productId, String buyerEmail) {
        return exchangeRepository
                .findByBuyerEmail(buyerEmail).stream()
                .filter(e -> e.getTargetProduct().getId().equals(productId))
                .findFirst()
                .map(e -> convertToDto(e, buyerEmail));
    }

}
