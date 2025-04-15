package com.github.mikolaali.currencyrate.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "cbrf")
public class CbProperties {

    private String url;
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

}
