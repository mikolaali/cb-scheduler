package com.github.mikolaali.currencyrate.repository;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class CbClient{

    @Timed
    public String getCurrencyRates(String  url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Couldn't connect to CBR.", e);
        }
    };
}
