package com.kessinger.kessinger.model.enums;

public enum Categoria {

    AC("Artigo Científico"),
    AR("Artigo de revisão"),
    AO("Artigo original"),
    MO("Monografia");

    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    Categoria(String categoria) {
        this.categoria = categoria;
    }
}
