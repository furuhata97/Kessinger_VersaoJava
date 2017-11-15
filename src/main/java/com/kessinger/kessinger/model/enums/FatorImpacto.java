package com.kessinger.kessinger.model.enums;

public enum FatorImpacto {

    A("0 - 1"),
    B("2 - 3"),
    C("4 - 5"),
    D("6 - 7"),
    E("8 - 9"),
    F("10");

    private String fator;

    public String getFator() {
        return fator;
    }

    FatorImpacto(String fator) {
        this.fator = fator;
    }
}
