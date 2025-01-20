package com.BankApplication.Bank.App.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class UserConfig {
     

     @Bean
     public static PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
          .csrf(csrf  -> csrf.disable())
          .authorizeHttpRequests(authz -> authz.requestMatchers("/register")
          .permitAll()
          .anyRequest().authenticated()
          )
          .formLogin(form -> form.loginPage("/login")
          .loginProcessingUrl("/login")
          .defaultSuccessUrl("/dashboard",true)
          .permitAll()
          )
          .logout(logout -> logout
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/login?logout")
          .permitAll()
          
          )
          .headers(header -> header
          .frameOptions(frameOptions -> frameOptions.sameOrigin())
                    );

          return http.build();
     } 

}
