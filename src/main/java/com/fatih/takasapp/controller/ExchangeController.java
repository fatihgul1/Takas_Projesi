package com.fatih.takasapp.controller;

import com.fatih.takasapp.dto.ExchangeDTO;
import com.fatih.takasapp.dto.ExchangeRequestDto;
import com.fatih.takasapp.entity.Exchange;
import com.fatih.takasapp.entity.ExchangeStatus;
import com.fatih.takasapp.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping
    public ResponseEntity<Exchange> createExchange(@RequestBody ExchangeRequestDto dto, Principal principal) {
        Exchange exchange = exchangeService.createExchange(
                dto.getOfferedProductId(),
                dto.getTargetProductId(),
                principal.getName()
        );
        return ResponseEntity.ok(exchange);
    }

    @GetMapping("/incoming")
    public List<ExchangeDTO> getIncoming(Principal principal) {
        return exchangeService.getIncomingExchanges(principal.getName());
    }

    @GetMapping("/outgoing")
    public List<ExchangeDTO> getOutgoing(Principal principal) {
        return exchangeService.getOutgoingExchanges(principal.getName());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam ExchangeStatus status) {
        exchangeService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/target/{productId}")
    public ResponseEntity<ExchangeDTO> getOfferForTarget(@PathVariable Long productId, Principal principal) {
        return exchangeService
                .getOfferForTarget(productId, principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

}