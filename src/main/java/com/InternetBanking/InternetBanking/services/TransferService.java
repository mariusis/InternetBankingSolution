package com.InternetBanking.InternetBanking.services;

import org.springframework.stereotype.Service;

@Service
public interface TransferService {

    public void transferFunds(String senderAccountNumber, String recipientAccountNumber, Double amount,String description);
}
