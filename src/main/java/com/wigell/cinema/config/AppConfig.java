package com.wigell.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


// Konfigurerar applikationens beans och inställningar.
// I det här fallet skapar vi en RestTemplate-bean som används för att göra HTTP-anrop
// till den gemensamma valutakonverteringsmikrotjänsten.
// Genom att definiera RestTemplate som en bean kan vi injicera den i CurrencyService
// och andra komponenter som behöver göra HTTP-anrop, vilket främjar återanvändning och testbarhet i applikationen.
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}