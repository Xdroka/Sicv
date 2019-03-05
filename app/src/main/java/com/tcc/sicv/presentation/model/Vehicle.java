package com.tcc.sicv.presentation.model;

public class Vehicle {
    private String imagem;
    private String modelo;
    private String potencia;
    private String preco;
    private String velocidadeMaxima;
    private String marca;
    private String tipo;
    private String codigo;

    public Vehicle(String imagem, String modelo, String potencia, String preco, String velocidadeMaxima, String marca, String tipo, String codigo) {
        this.imagem = imagem;
        this.modelo = modelo;
        this.potencia = potencia;
        this.preco = preco;
        this.velocidadeMaxima = velocidadeMaxima;
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

    public String getVelocidadeMaxima() {
        return velocidadeMaxima;
    }

    public String getMarca() {
        return marca;
    }

    public String getTipo() {
        return tipo;
    }
}
