package com.BankApplication.Bank.App.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BankApplication.Bank.App.Entity.Transaction;
import com.BankApplication.Bank.App.Entity.User;
import com.BankApplication.Bank.App.Repository.TransactionRepository;
import com.BankApplication.Bank.App.Repository.UserRepository;

@Service
//
public class UserService implements UserDetailsService {
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
               User user) throws Exception {
          // System.out.println(user.toString());
          if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
               throw new Exception("User already Exists");
          }
          user.setBalance(BigDecimal.ZERO);
          user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userRepository.save(user);
          // return user;
     }

     // Deposit transaction
     public void deposit(User user, BigDecimal amount) {
          // Balance added to the user account
          user.setBalance(user.getBalance().add(amount));
          userRepository.save(user);

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
          return transactionRepository.findByUser_AccountNumber(user.getAccountNumber());
     }

     // Transfering amount to another user/account number
     public void transferAmount(User fromUser, long toAccountNumber, BigDecimal amount) {
          if (fromUser.getBalance().compareTo(amount) < 0) {
               throw new RuntimeException("Insufficient Balance!");
          }

          User toUser = userRepository.findById(toAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));

          fromUser.setBalance(fromUser.getBalance().subtract(amount));
          toUser.setBalance(toUser.getBalance().add(amount));

          // Create transaction records
          Transaction senderTransaction = new Transaction();
          senderTransaction.setUser(fromUser);
          senderTransaction.setAmount(amount.negate());
          senderTransaction.setType("Transfer");
          senderTransaction.setTimestamp(LocalDateTime.now());
          transactionRepository.save(senderTransaction);

          Transaction recipientTransaction = new Transaction();
          recipientTransaction.setUser(toUser);
          recipientTransaction.setAmount(amount);
          recipientTransaction.setType("Transfer");
          recipientTransaction.setTimestamp(LocalDateTime.now());
          transactionRepository.save(recipientTransaction);

          userRepository.save(fromUser);
          userRepository.save(toUser);
     }


     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          User user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

          return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    getAuthorities("USER") // Assign appropriate roles
          );
     }

     private Collection<? extends GrantedAuthority> getAuthorities(String role) {
          return List.of(new SimpleGrantedAuthority(role));
     }

}
