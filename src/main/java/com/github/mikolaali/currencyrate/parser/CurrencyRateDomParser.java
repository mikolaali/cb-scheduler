package com.github.mikolaali.currencyrate.parser;


import com.github.mikolaali.currencyrate.model.CurrencyRate;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyRateDomParser {

    // class API
    public List<CurrencyRate> parse(String ratesAsXmlString) {
        List<CurrencyRate> currencyRates;
        DocumentBuilder documentBuilder;
        Document document;

        try {
            documentBuilder = createDocumentBuilder();
            document = createDocument(documentBuilder, ratesAsXmlString);

            NodeList nodeList = document.getElementsByTagName("Valute");

            currencyRates = extractCurrencyRateListFromNodeList(nodeList);

        } catch (Exception e) {
            System.err.println("Couldn't parse rates as XML.");
            throw new RuntimeException(e);
        }
        return currencyRates;
    }

    // API impl
    private DocumentBuilder createDocumentBuilder() {
        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder documentBuilder;

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Couldn't initialize XML parser." +
                                       "with feature 'XMLConstants.FEATURE_SECURE_PROCESSING'");
        }
        return documentBuilder;
    }

    private Document createDocument(DocumentBuilder documentBuilder, String ratesAsXmlString) {
        Document document;
        try (StringReader reader = new StringReader(ratesAsXmlString)) {
            document = documentBuilder.parse(new InputSource(reader));
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't parse rates as XML.");
        }
        return document;
    }

    private static List<CurrencyRate> extractCurrencyRateListFromNodeList(NodeList nodeList) {
        List<CurrencyRate> rates = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            var node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var element = (Element) node;
                var currencyRate = CurrencyRate.builder()
                        .numCode(element.getElementsByTagName("NumCode").item(0).getTextContent())
                        .charCode(element.getElementsByTagName("CharCode").item(0).getTextContent())
                        .nominal(element.getElementsByTagName("Nominal").item(0).getTextContent())
                        .name(element.getElementsByTagName("Name").item(0).getTextContent())
                        .value(element.getElementsByTagName("Value").item(0).getTextContent())
                        .vuniRate(element.getElementsByTagName("VunitRate").item(0).getTextContent())
                        .build();
                rates.add(currencyRate);
            }
        }
        return rates;
    }
}