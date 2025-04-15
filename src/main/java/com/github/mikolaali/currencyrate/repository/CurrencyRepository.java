package com.github.mikolaali.currencyrate.repository;

import com.github.mikolaali.currencyrate.model.CurrencyRate;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<CurrencyRate, Long> {
    @Timed
    List<CurrencyRate> findAll();
}
