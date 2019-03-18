package com.tcc.sicv.data.model

data class Ticket(
        val custoTotal: String, val tipo: String, val codigoVeiculo: String,
        var time: String?, var ticketId: String?, val dataAgendada: String
)
