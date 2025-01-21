package com.BankApplication.Bank.App.Entity;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private long accountNumber;
     private String fullName;
     
     private String email;
     private String mobileNumber;
     private String password;
     private long pin;
     private BigDecimal balance;

     @OneToMany(mappedBy = "user")
     List<Transaction> transactions;

}
