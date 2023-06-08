package com.InternetBanking.InternetBanking.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId ;
    private String accountName;
    private static final String ACCOUNT_TYPE_RO = "RO40";
    @Column(name = "account_number", unique = true)
    private String accountNumber;
    private String accountType;
    private String currency;
    private Double balance = 0.00;

    private Long ownerId;
    public Account() {
        // Generate a unique account number
        String baseNumber = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 10);
        String prefix = ACCOUNT_TYPE_RO; // Change this depending on the account type
        this.accountNumber = prefix + baseNumber;
    }

    public Account(String accountType, Long ownerId,String accountName,String currency) {

        this.accountType = accountType;
        this.ownerId = ownerId;
        this.accountName = accountName;
        String baseNumber = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 12);
        String prefix = ACCOUNT_TYPE_RO; // Change this depending on the account type
        this.accountNumber = prefix + baseNumber;
        this.currency= currency;
    }
    private void send(Double amount){
        this.balance = this.balance - amount;
    }
    private void receive(Double amount){
        this.balance = this.balance + amount;
    }
    public void deposit(Double amount){
        this.balance = this.balance + amount;
    }
    public void transfer(Account recipient, Double amount) {
       if(this.currency != recipient.getCurrency()){
           throw new IllegalArgumentException("Wrong currency");
       }

        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        if(recipient == null){
            throw new IllegalArgumentException("Invalid Account Number");
        }
        this.send(amount);
        recipient.receive(amount);
    }


}
