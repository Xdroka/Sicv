package com.tcc.sicv.data.model

data class Vehicle(
        val imagem: String, val modelo: String, val potencia: String, val preco: String,
        val velocidade: String, val marca: String, val tipo: String, var codigo: String?,
        var estaReparando: Boolean?) {
    var manutencao: Boolean? = false
}
