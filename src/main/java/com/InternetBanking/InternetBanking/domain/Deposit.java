package com.InternetBanking.InternetBanking.domain;

import com.InternetBanking.InternetBanking.repositories.AccountRepository;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deposit")
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountName;
    private String accountNumber;

    @NotNull
    private Double amount;
    private String currency;
    @NotNull
    private LocalDateTime date;

    public Deposit(String accountNumber,String accountName , Double amount,String currency) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.amount = amount;
        this.currency = currency;
        this.date = LocalDateTime.now();
    }
}

