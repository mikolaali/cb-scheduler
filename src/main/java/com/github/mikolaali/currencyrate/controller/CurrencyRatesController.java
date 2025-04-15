package com.github.mikolaali.currencyrate.controller;

import com.github.mikolaali.currencyrate.model.CurrencyRate;
import com.github.mikolaali.currencyrate.service.CbApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CurrencyRatesController {

    private final CbApiService cbService;

    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyRate>> getAllRates() {
        return ResponseEntity.ok(
                cbService.getAllRates()
        );
    }

}
