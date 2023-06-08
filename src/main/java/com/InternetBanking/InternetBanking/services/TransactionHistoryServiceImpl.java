package com.InternetBanking.InternetBanking.services;

import com.InternetBanking.InternetBanking.domain.*;
import com.InternetBanking.InternetBanking.repositories.AccountRepository;

import com.InternetBanking.InternetBanking.repositories.DepositRepository;
import com.InternetBanking.InternetBanking.repositories.TransferRepository;
import com.InternetBanking.InternetBanking.repositories.UserRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private final TransferRepository transferRepository;
    private final DepositRepository depositRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public TransactionHistoryServiceImpl(TransferRepository transferRepository, DepositRepository depositRepository, UserRepository userRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.depositRepository = depositRepository;

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
    public List<TransactionInfo> findAllByUser(String username){

        User user = userRepository.findByEmail(username);
        List<Account> accounts = accountRepository.findAllByOwnerId(user.getUserId());
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transfer> transfers = transferRepository.findAllBySenderAccountIdInOrReceiverAccountIdIn(accountIds,accountIds);
        List<String> accountNumbers = accounts.stream().map(Account::getAccountNumber).collect(Collectors.toList());
        List<Deposit> deposits = depositRepository.findAllByAccountNumberIn(accountNumbers);
        List<TransactionInfo> transactionInfoList = new ArrayList<>();

        
        for (Deposit deposit : deposits) {
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setId(deposit.getId());
            transactionInfo.setSource("ATM");
            transactionInfo.setDateTime(deposit.getDate());
            transactionInfo.setAccountNumber(deposit.getAccountNumber());
            transactionInfo.setAccountName(deposit.getAccountName());
            transactionInfo.setAmount(deposit.getAmount());
            transactionInfo.setTransactionType("DEPOSIT");
            transactionInfo.setDescription("ATM deposit from <location> ");
            transactionInfo.setCurrency(deposit.getCurrency());
            transactionInfoList.add(transactionInfo);
        }
        
        for (Transfer transfer : transfers) {
            TransactionInfo transferInfo = new TransactionInfo();
            transferInfo.setId(transfer.getTransferId());
            transferInfo.setSource(transfer.getSender().getAccountName());
            transferInfo.setDateTime(transfer.getDate());
            transferInfo.setAccountNumber(transfer.getReceiverName());
            transferInfo.setAccountName(transfer.getReceiver().getAccountName());
            transferInfo.setAmount(transfer.getAmount());
            transferInfo.setTransactionType("TRANSFER");
            transactionInfoList.add(transferInfo);
            transferInfo.setDescription(transfer.getDescription());
        }
        Collections.sort(transactionInfoList, Comparator.comparing(TransactionInfo::getDateTime));
        
        return transactionInfoList;
    }

    public String getAccountStatement(List<TransactionInfo> transactions) throws DocumentException, IOException {
        StringBuilder statementBuilder = new StringBuilder();

        // Adaugă antetul extrasului de cont
        statementBuilder.append("EXTRAS DE CONT\n");
        statementBuilder.append("----------------------------------------\n");
        statementBuilder.append("Data                            | Descriere                                | Suma\n");
        statementBuilder.append("----------------------------------------\n");

        // Parcurge lista de tranzacții și adaugă informațiile în extrasul de cont
        for (TransactionInfo transaction : transactions) {
            statementBuilder.append(transaction.getDateTime())
                    .append(" | ")
                    .append(transaction.getDescription())
                    .append(" | ")
                    .append(transaction.getAmount())
                    .append("\n");
        }

        // Adaugă totalul extrasului de cont
        double totalAmount = transactions.stream()
                .mapToDouble(TransactionInfo::getAmount)
                .sum();
        statementBuilder.append("----------------------------------------\n");
        statementBuilder.append("Total: ")
                .append(totalAmount);

        // Returnează extrasul de cont sub forma unui șir de caractere
        generatePDF(statementBuilder.toString());

        // Returnează numele fișierului PDF generat
        return "account_statement.pdf";
    }

    public void generatePDF(String accountStatement) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("account_statement.pdf"));
        document.open();

        // Adaugă font-ul pentru antet
        Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        // Adaugă antetul extrasului de cont

        // Adaugă conținutul extrasului de cont
        document.add(new Paragraph(accountStatement));

        // Adaugă totalul extrasului de cont


        document.close();
    }
}
