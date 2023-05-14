package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Deposit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepositRepository extends CrudRepository<Deposit,Long> {

    List<Deposit> findAllByAccountNumberIn(List<String> accountNumbers);
}
