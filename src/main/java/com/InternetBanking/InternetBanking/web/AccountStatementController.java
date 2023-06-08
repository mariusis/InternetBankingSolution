package com.InternetBanking.InternetBanking.web;

import com.InternetBanking.InternetBanking.domain.TransactionInfo;
import com.InternetBanking.InternetBanking.domain.User;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.InternetBanking.InternetBanking.services.TransactionHistoryServiceImpl;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class AccountStatementController {


    private final TransactionHistoryServiceImpl transactionHistoryService;
    private final UserRepository userRepository;

    public AccountStatementController(TransactionHistoryServiceImpl transactionHistoryService, UserRepository userRepository) {
        this.transactionHistoryService = transactionHistoryService;
        this.userRepository = userRepository;
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadAccountStatement() throws DocumentException, IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(username);
        List<TransactionInfo> transactions = transactionHistoryService.findAllByUser(username);
        String fileName = transactionHistoryService.getAccountStatement(transactions); // Folosește metoda getAccountStatement pentru a genera fișierul PDF
        File file = new File(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=account_statement.pdf");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new FileSystemResource(file));
    }
}