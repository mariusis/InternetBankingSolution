package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.TransactionInfo;
import com.InternetBanking.InternetBanking.domain.Transfer;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TransactionHistoryService {
    public List<TransactionInfo> findAllByUser(String username);

}
