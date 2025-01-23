package com.BankApplication.Bank.App.Configuration;

import java.util.function.LongFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.BankApplication.Bank.App.Services.UserService;

@Configuration
@EnableWebSecurity
public class UserConfig {

     @Autowired
     private UserService userService;

     @Bean
     public static PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }

     @Bean
     public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
          return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userService)
                    .passwordEncoder(passwordEncoder())
                    .and()
                    .build();
     }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                    .csrf(csrf -> csrf.disable()) // Consider enabling CSRF protection in production
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(
                              auth -> auth
                                        .requestMatchers("/login", "/register", "/error", "/style.css", "/js/**", "/images/**","/test").permitAll()
                                        .anyRequest().authenticated())
                    .formLogin(form -> form.loginPage("/login")
                              .loginProcessingUrl("/login")
                              .defaultSuccessUrl("/dashboard", true)
                              .permitAll())
                    .logout(logout -> logout.invalidateHttpSession(true)
                              .clearAuthentication(true)
                              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                              .logoutSuccessUrl("/login?logout")
                              .permitAll())
                    .build();
     }
}
