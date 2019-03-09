package com.tcc.sicv.data.model;

public class Ticket {
    private String custoTotal;
    private String tipo;
    private String codigoVeiculo;
    private String time;
    private String ticketId = "";

    public Ticket(String custoTotal, String tipo, String codigoVeiculo, String time, String ticketId) {
        this.custoTotal = custoTotal;
        this.tipo = tipo;
        this.codigoVeiculo = codigoVeiculo;
        this.time = time;
        this.ticketId = ticketId;
    }

    public String getCustoTotal() {
        return custoTotal;
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

    public void setTime(String time) { this.time = time; }

    public String getTicketId() { return ticketId; }

    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
}
