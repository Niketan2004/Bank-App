package com.BankApplication.Bank.App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BankAppController {
     
     // Shows Register form to the user
     @GetMapping("/register")
     public String registerForm() {
          return "register";
     }
     // Shows Login  form to the user
     @GetMapping("/login")
     public String loginForm() {
          return "login";
     }

// register user to database and redirects it to dashboard
     @PostMapping("/register")
     public String registerUser() {
          

          return "dashboard";
     }


}
