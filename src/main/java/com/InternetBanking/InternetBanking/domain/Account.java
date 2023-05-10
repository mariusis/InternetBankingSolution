package com.InternetBanking.InternetBanking.domain;

import lombok.*;

import javax.persistence.*;
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId ;
    @Transient
    private static int sequenceNumber = 1;
    @Column(name = "account_number")
    String accountNumber = String.format("%09d", sequenceNumber); // generates "000000001"

    private String accountType;
    private Double balance = 0.00;


    private Long ownerId;


    public Account(String accountType, Long ownerId) {

        this.accountType = accountType;
        this.ownerId = ownerId;
        sequenceNumber++;

    }


}
