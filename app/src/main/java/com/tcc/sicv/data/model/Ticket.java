package com.tcc.sicv.data.model;

public class Ticket {
    private String valorDoVeiculo;
    private String tipo;
    private String codigoVeiculo;
    private String data;

    public Ticket(String valorDoVeiculo, String tipo, String codigo, String data) {
        this.valorDoVeiculo = valorDoVeiculo;
        this.tipo = tipo;
        this.codigoVeiculo = codigo;
        this.data = data;
    }

    public String getValorDoVeiculo() {
        return valorDoVeiculo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCodigoVeiculo() {
        return codigoVeiculo;
    }

    public String getData() {
        return data;
    }
}
