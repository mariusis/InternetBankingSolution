package com.InternetBanking.InternetBanking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;
    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName="accountId")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName="accountId")
    private Account receiver;
    private Double amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private String senderName;
    private String receiverName;
    private String description;

    public Transfer(Account sender, Account receiver, Double amount,String senderName,String receiverName,String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.description = description;
    }
}