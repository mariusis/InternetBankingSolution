package com.InternetBanking.InternetBanking.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {
    private String AccountNumber;
    private Double amount;

    // getters and setters
}