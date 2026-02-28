package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example")
public class SpringConfig {
    @Bean
    public User user(){
        return new User("user1","John Doe");
    }

    @Bean
    public BankService bankService(){
        return new BankService();
    }
}
