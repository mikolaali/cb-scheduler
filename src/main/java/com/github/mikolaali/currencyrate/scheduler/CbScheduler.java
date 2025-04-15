package com.github.mikolaali.currencyrate.scheduler;

import com.github.mikolaali.currencyrate.service.CbApiService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CbScheduler {

    private final CbApiService cbApiService;

    @Value("${spring.app.cbrf.url}")
    private String url;

    private LocalDate startDate = LocalDate.now();

    private int daysCount = 0;

    // class API
    @Timed
    @Scheduled(fixedDelayString = "${spring.app.scheduler.fixedDelay}")
    public void getCurrencyRate() {
        LocalDate date = getNextDate();
        String url = createUrlWithDate(date);
        cbApiService.getCurrencyRate(url, date);
    }

    // implementation
    private String createUrlWithDate(LocalDate date) {
        return String.format("%s?date_req=%s", url,
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date));
    }

    private LocalDate getNextDate() {
        return startDate.minusDays(daysCount++);
    }
}
