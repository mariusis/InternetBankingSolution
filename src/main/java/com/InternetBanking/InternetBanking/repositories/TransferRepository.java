package com.InternetBanking.InternetBanking.repositories;

import com.InternetBanking.InternetBanking.domain.Transfer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Long> {

    List<Transfer> findAllBySenderAccountIdInOrReceiverAccountIdIn(List<Long> accountIds,List<Long> accountIds2);
}