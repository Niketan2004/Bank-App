package com.BankApplication.Bank.App.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BankApplication.Bank.App.Entity.Transaction;
import com.BankApplication.Bank.App.Entity.User;
import com.BankApplication.Bank.App.Repository.TransactionRepository;
import com.BankApplication.Bank.App.Repository.UserRepository;

@Service
public class UserService {
     @Autowired
     PasswordEncoder passwordEncoder;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private TransactionRepository transactionRepository;

     // finds user by there email id to verify the user exists or not
     public Optional<User> findUser(String email) {
          return userRepository.findUserByEmail(email);
     }

     // Registers new user in database if it is not exists
     public User registerUser(
               String name, String email, String mobile,
               String password,
               long pin) throws Exception

     {
          if (userRepository.findUserByEmail(email).isPresent()) {
               throw new Exception("User already Exists");
          }
          User user = new User();
          user.setFullName(name);
          user.setEmail(email);
          user.setMobileNumber(mobile);
          user.setPassword(passwordEncoder.encode(password));
          user.setPin(pin);
          user.setBalance(BigDecimal.ZERO);

          userRepository.save(user);
          return user;
     }

     // Deposit transaction
     public void deposit(User user, BigDecimal amount) {
          // Balance added to the user account
          user.setBalance(user.getBalance().add(amount));

          Transaction transaction = new Transaction();
          // Setting transaction records
          transaction.setAmount(amount);
          transaction.setType("Depoist");
          transaction.setTimestamp(LocalDateTime.now());
          transaction.setUser(user);
          transactionRepository.save(transaction);
     }

     // withdraw amount
     public void withdraw(User user, BigDecimal amount) throws Exception {
          if (user.getBalance().compareTo(amount) < 0) {
               throw new Exception("Insufficiant Balance");
          }
          // Useer has sufficiant balance then it will be withdrawn and updates the user
          // balance
          user.setBalance(user.getBalance().subtract(amount));
          userRepository.save(user);

          // Creating transaction record for this transaction
          Transaction transaction = new Transaction();
          transaction.setAmount(amount);
          transaction.setTimestamp(LocalDateTime.now());
          transaction.setType("Withdraw");
          transaction.setUser(user);

          transactionRepository.save(transaction);

     }

     // Returns list of all the transactions
     public List<Transaction> transactionHistory(User user) {
          return transactionRepository.findByAccountNumber(user.getAccountNumber());
     }

     // Transfering amount to another user/account number
     public void transferAmount(User fromuser, long toAccountNumber, BigDecimal amount) {
          // checks recivers account exists or not
          if (fromuser.getBalance().compareTo(amount) < 0) {
               throw new RuntimeException("Insufficiant Balance !");
          }
          // if exists then it will add amount to reciever and subtracts amount from
          // sender
          User user = userRepository.findById(toAccountNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));
          // setting balance of recievers account
          user.setBalance(user.getBalance().add(amount));
          userRepository.save(user);
          // setting balance of senders account by subtracting amount sent
          fromuser.setBalance(fromuser.getBalance().subtract(amount));
          userRepository.save(fromuser);

     }
}

// private String fullName;
// private String email;
// private String mobileNumber;
// private String password;
// private long pin;

// private long transactionId;
// private BigDecimal amount;
// private String type;
// private LocalDateTime timestamp;

// @ManyToOne
// @JoinColumn(name = "account_number")
// private User user;
