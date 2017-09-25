package com.kessinger.kessinger.model.enums;

public enum Sexo {

    M("Masculino"), F("Feminino"), OUTRO("Outro");

    private String sexo;

    public String getSexo() {
        return sexo;
    }

    Sexo(String sexo) {
        this.sexo = sexo;
    }
}
