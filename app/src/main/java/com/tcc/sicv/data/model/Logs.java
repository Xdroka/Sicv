package com.tcc.sicv.data.model;

public class Logs {
    private String data;
    private String descricao;
    private String gasto;

    public Logs(String data, String descricao, String gasto) {
        this.data = data;
        this.descricao = descricao;
        this.gasto = gasto;
    }

    public String getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getGasto() {
        return gasto;
    }
}
