package com.kirinalex.BankBackOffice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:currate-${spring.profiles.active}.properties")
public class CurrateConfig {

}
