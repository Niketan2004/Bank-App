package com.BankApplication.Bank.App.Entity;

import org.springframework.data.annotation.Transient;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long accountNumber;
     private String fullName;
     @Column(unique = true)
     private String email;
     @Column(unique = true)
     private String mobileNumber;
     private String password;
     private long pin;
     private BigDecimal balance;

     @Transient
     private Collection<? extends GrantedAuthority> authorities;

     @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     private List<Transaction> transactions;

     public User(String username, String password, BigDecimal balance, List<Transaction> transactions,
               Collection<? extends GrantedAuthority> authorities) {
          this.email = username;
          this.password = password;
          this.balance = balance;
          this.transactions = transactions;
          this.authorities = authorities;
    }
}
