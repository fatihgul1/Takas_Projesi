package com.fatih.takasapp.dto;

import lombok.Data;

@Data
public class ExchangeRequestDto {
    private Long offeredProductId;
    private Long targetProductId;
}