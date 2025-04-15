package com.github.mikolaali.currencyrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
public class SchedulerCbCurrencyRateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerCbCurrencyRateApplication.class, args);
    }

}
