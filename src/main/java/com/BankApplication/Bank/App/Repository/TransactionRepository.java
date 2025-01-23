package com.BankApplication.Bank.App.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankApplication.Bank.App.Entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

      List<Transaction> findByUser_AccountNumber(long accountNumber);
     
}
