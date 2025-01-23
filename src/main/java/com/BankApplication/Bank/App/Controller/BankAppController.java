package com.BankApplication.Bank.App.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

     // @Autowired
     // private AuthenticationManager authenticationManager;
     @GetMapping("/test")
     public String test() {
          return "test";
     }

     // Shows Register form to the user
     @GetMapping("/register")
     public String showRegisterPage() {
          return "register"; // Ensure there's a "register.html" in the `templates` folder
     }

     // Shows Login form to the user
     @GetMapping("/login")
     public String loginForm() {
          return "login";
     }

     // Shows dashboard to the user
     @GetMapping("/dashboard")
     public String dashboard(Model model) {
          String username = SecurityContextHolder.getContext().getAuthentication().getName();
          User user = userService.findUser(username).orElseThrow();
          model.addAttribute("User", user);

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
     @GetMapping("/transactions")
     public String showTransactions(Model model) {
          String username = SecurityContextHolder.getContext().getAuthentication().getName();
          User user = userService.findUser(username).orElseThrow();

          model.addAttribute("Transaction", userService.transactionHistory(user));

          return "transaction";
     }

     // register user to database and redirects it to dashboard
     @PostMapping("/register-user")
     public String registerUser(@ModelAttribute User user, Model model) {
          System.out.println(user.getEmail());
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
     // @PostMapping("/login")
     // public String loginUser(@ModelAttribute User user, Model model) {
     // // // Perform authentication using Spring Security mechanisms
     // // // (e.g., username/password authentication)
     // Authentication authentication = authenticationManager.authenticate(
     // new UsernamePasswordAuthenticationToken(user.getEmail(),
     // user.getPassword()));

     // SecurityContextHolder.getContext().setAuthentication(authentication);

     // return "redirect:/dashboard";
     // }

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

     // // deposit method to deposit money
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

     // // transfer method to traansfer money
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
