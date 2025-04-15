package com.github.mikolaali.currencyrate.service;

import com.github.mikolaali.currencyrate.model.CurrencyRate;
import com.github.mikolaali.currencyrate.parser.CurrencyRateDomParser;
import com.github.mikolaali.currencyrate.repository.CbClient;
import com.github.mikolaali.currencyrate.repository.CurrencyRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CbApiService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CbApiService.class);
    // url
    // client
    // repo
    // return
    private final CbClient cbClient;

    private final CurrencyRepository currencyRepository;

    private final CurrencyRateDomParser parser;

    @Value("${spring.app.cbrf.url}")
    private String url;

    @Value("${spring.app.cbrf.date}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private AtomicInteger currencyAmountPerRequestMetric =  new AtomicInteger(0);
    private AtomicInteger currencyAmountTotalMetric =  new AtomicInteger(0);

    public CbApiService(CbClient cbClient, CurrencyRepository currencyRepository, CurrencyRateDomParser parser, MeterRegistry meterRegistry) {
        this.cbClient = cbClient;
        this.currencyRepository = currencyRepository;
        this.parser = parser;
        meterRegistry.gauge("currencyAmountPerRequest", currencyAmountPerRequestMetric);
        meterRegistry.gauge("currencyAmountTotal", currencyAmountTotalMetric);
    }

    // class API
    public List<CurrencyRate> getAllRates() {
        return currencyRepository.findAll();
    }

    public ResponseEntity<List<CurrencyRate>> getCurrencyRate(String urlWithDate, LocalDate date) {
        var ratesList = cbClient.getCurrencyRates(urlWithDate);
        var rates = parser.parse(ratesList);
        enrichRatesWithDate(rates, date);
        setMetrics(rates);
        currencyRepository.saveAll(rates);
        return ResponseEntity.ok(rates);

    }

    private void setMetrics(List<CurrencyRate> rates) {
        currencyAmountPerRequestMetric.set(rates.size());
        var total = currencyAmountTotalMetric.get();
        currencyAmountTotalMetric.set(total + rates.size());
    }

    // implementation
    private void enrichRatesWithDate(List<CurrencyRate> rates, LocalDate date) {
        rates.forEach(rate -> rate.setDate(date));
    }
}