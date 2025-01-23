package com.BankApplication.Bank.App.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.BankApplication.Bank.App.Entity.User;
import com.BankApplication.Bank.App.Services.UserService;

@Controller
public class BankAppController {
     @Autowired
     private UserService userService;

     // Shows Register form to the user
     @GetMapping("/register")
     public String registerForm() {
          return "register";
     }

     // Shows Login form to the user
     @GetMapping("/login")
     public String loginForm() {
          return "login";
     }

     // Shows dashboard to the user
     @GetMapping("/dashboard")
     public String dashboard() {
          return "dashboard";
     }

     // Shows deposit page to the user
     @GetMapping("/deposit")
     public String deposit() {
          return "deposit-withdraw";
     }

     // Shows withdraw page to the user
     @GetMapping("/withdraw")
     public String withdraw() {
          return "deposit-withdraw";
     }

     // Shows transfer page to the user
     @GetMapping("/transfer")
     public String transfer() {
          return "transfer";
     }

     // Showing the list of all the transactions to user
     // @GetMapping("/transactions")
     // public String showTransactions(Model model)
     // {
     //      String username = SecurityContextHolder.getContext().getAuthentication().getName();
     //      User user = userService.findUser(username).orElseThrow();

     //      model.addAttribute("Tranasaction",userService.transactionHistory(user));

     //      return "transaction";
     // }

     // register user to database and redirects it to dashboard
     @PostMapping("/register")
     public String registerUser(@ModelAttribute User user, Model model) {
          try {
               userService.registerUser(user.getFullName(), user.getEmail(), user.getMobileNumber(), user.getPassword(),
                         user.getPin());

               return "redirect:/login";
          } catch (Exception e) {
               model.addAttribute("error", e.getMessage());
               return "redirect:/register";
          }

     }

     // Verify user when login and if exists redirect it to dashboard
     @PostMapping("/login")
     public String loginUser(Model model) { // @ModelAttribute User user,
          String email = SecurityContextHolder.getContext().getAuthentication().getName();

          User user2 = userService.findUser(email).orElseThrow(() -> new RuntimeException("User not found"));
          model.addAttribute("User", user2);
          return "dashboard";
     }

     // deposit method to deposit money
     @PostMapping("/deposit")
     public String depositAmount(@RequestAttribute BigDecimal amount, @RequestAttribute long pin) {

          String username = SecurityContextHolder.getContext().getAuthentication().getName();
          User user = userService.findUser(username).orElseThrow();
          if (user.getPin() == pin) {
               userService.deposit(user, amount);
               return "redirect:/dashboard";

          } else {
               return "redirect:/deposit";
          }
     }

     // deposit method to deposit money
     @PostMapping("/withdraw-amount")
     public String withdrawAmount(@RequestAttribute BigDecimal amount, @RequestAttribute long pin, Model model) {
          // gets username
          String username = SecurityContextHolder.getContext().getAuthentication().getName();
          // checks user of username is present or not
          User user = userService.findUser(username).orElseThrow();
          // if user is present then it will validate the pin and does money transfer
          try {
               if (user.getPin() == pin) {
                    userService.withdraw(user, amount);
                    return "redirect:/dashboard";
               }
          } catch (Exception e) {
               model.addAttribute("error", e.getMessage());
               return "redirect:/withdraw";
          }
          return "redirect:/dashboard";

     }

     // deposit method to deposit money
     @PostMapping("/transfer")
     public String transferAmount(@RequestAttribute long accountnumber, @RequestAttribute BigDecimal amount,
               @RequestAttribute long pin, Model model) {
          // gets username
          String username = SecurityContextHolder.getContext().getAuthentication().getName();
          // checks user of username is present or not
          User user = userService.findUser(username).orElseThrow();
          // if user is present then it will validate the pin and does money transfer
          try {
               if (user.getPin() == pin) {
                    userService.transferAmount(user, accountnumber, amount);
               }
          } catch (Exception e) {
               model.addAttribute("error", e.getMessage());
               return "redirect:/transfer";
          }
          return "redirect:/dashboard";

     }

}
