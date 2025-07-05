package com.fatih.takasapp.dto;

import com.fatih.takasapp.entity.ExchangeStatus;
import com.fatih.takasapp.entity.User;
import lombok.Data;

@Data
public class ExchangeDTO {
    private Long id;
    private String offeredProductName;
    private String targetProductName;
    private User buyer;
    private User seller;
    private ExchangeStatus status;
    private String contactPhone;
}
