package com.InternetBanking.InternetBanking.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private Double amount;
    private String description;

    // getters and setters
}