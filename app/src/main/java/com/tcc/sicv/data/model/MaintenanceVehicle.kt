package com.tcc.sicv.data.model

data class MaintenanceVehicle(
        val maintenanceCode: String,
        val modelo: String,
        val imagem: String,
        val cod_veiculo: String,
        val veiculo_liberado: Boolean? = false
)
