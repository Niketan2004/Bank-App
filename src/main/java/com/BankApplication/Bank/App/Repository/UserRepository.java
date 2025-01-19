package com.BankApplication.Bank.App.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankApplication.Bank.App.Entity.User;

public interface UserRepository  extends JpaRepository<User,Long>{
     
}
