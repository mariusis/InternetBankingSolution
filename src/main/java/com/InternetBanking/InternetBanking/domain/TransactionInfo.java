package com.InternetBanking.InternetBanking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class TransactionInfo {
    private Long id;
    private String accountName;
    private String source;
    private LocalDateTime dateTime;
    private String accountNumber;
    private Double amount;
    private String transactionType;


}