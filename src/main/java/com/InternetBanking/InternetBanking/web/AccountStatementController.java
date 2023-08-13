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

    private final TransactionHistoryServiceImpl transactionHistoryService; // Service to handle transaction history logic
    private final UserRepository userRepository; // Repository for accessing user data

    // Constructor Dependency Injection (DI)
    public AccountStatementController(TransactionHistoryServiceImpl transactionHistoryService, UserRepository userRepository) {
        this.transactionHistoryService = transactionHistoryService; // Initializing TransactionHistoryService via DI
        this.userRepository = userRepository; // Initializing UserRepository via DI
    }

    // Handles the route for downloading the account statement as a PDF file
    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadAccountStatement() throws DocumentException, IOException {
        // Get the username of the currently authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the User instance using the UserRepository
        User user = userRepository.findByEmail(username);

        // Retrieve the list of TransactionInfo instances associated with the user
        List<TransactionInfo> transactions = transactionHistoryService.findAllByUser(username);

        // Generate the account statement PDF file and get its filename
        String fileName = transactionHistoryService.getAccountStatement(transactions);

        // Create a File object based on the generated filename
        File file = new File(fileName);

        // Set HTTP headers for the download response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=account_statement.pdf");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        // Build the ResponseEntity with the PDF content for download
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new FileSystemResource(file));
    }
}