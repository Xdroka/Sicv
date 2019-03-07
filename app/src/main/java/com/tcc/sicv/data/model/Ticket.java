package com.tcc.sicv.data.model;

public class Ticket {
    private String valorDoVeiculo;
    private String tipo;
    private String codigoVeiculo;
    private String time;

    public Ticket(String valorDoVeiculo, String tipo, String codigo, String time) {
        this.valorDoVeiculo = valorDoVeiculo;
        this.tipo = tipo;
        this.codigoVeiculo = codigo;
        this.time = time;
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

    public String getTime() {
        return time;
    }
}
