package com.BankApplication.Bank.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankApplication.Bank.App.Entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
     
}
