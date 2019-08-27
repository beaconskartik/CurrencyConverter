package com.example.currencyconnector.models;

public class Currency {

    private String codeName;
    private Double value;

    Currency(String codeName, Double value) {
        this.codeName = codeName;
        this.value = value;
    }

    public String getCodeName() {
        return codeName;
    }

    public Double getValue() {
        return value;
    }
}

