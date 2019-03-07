package com.tcc.sicv.data.model;

public class Ticket {
    private String valorDoVeiculo;
    private String tipo;
    private String codigo;
    private String data;

    public Ticket(String valorDoVeiculo, String tipo, String codigo, String data) {
        this.valorDoVeiculo = valorDoVeiculo;
        this.tipo = tipo;
        this.codigo = codigo;
        this.data = data;
    }

    public String getValorDoVeiculo() {
        return valorDoVeiculo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getData() {
        return data;
    }
}
