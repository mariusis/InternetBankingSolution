package com.InternetBanking.InternetBanking.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String accountType;
    private String email;
    private String accountName;
    private String currency;


}
