package com.tcc.sicv.data.model;

public class Vehicle {
    private String imagem;
    private String modelo;
    private String potencia;
    private String preco;
    private String velocidade;
    private String marca;
    private String tipo;
    private String codigo;

    public Vehicle(String imagem, String modelo, String potencia, String preco,
                   String velocidade, String marca, String tipo, String codigo) {
        this.imagem = imagem;
        this.modelo = modelo;
        this.potencia = potencia;
        this.preco = preco;
        this.velocidade = velocidade;
        this.marca = marca;
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getImagem() {
        return imagem;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPotencia() {
        return potencia;
    }

    public String getPreco() {
        return preco;
    }

    public String getVelocidade() {
        return velocidade;
    }

    public String getMarca() {
        return marca;
    }

    public String getTipo() {
        return tipo;
    }
}
