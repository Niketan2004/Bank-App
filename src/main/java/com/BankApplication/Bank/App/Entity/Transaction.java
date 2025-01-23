package com.BankApplication.Bank.App.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long transactionId;
     private BigDecimal amount;
     private String type;
     private LocalDateTime timestamp;
  
     @ManyToOne(optional = false)
     @JoinColumn(name = "accountNumber")
     private User user;

}
