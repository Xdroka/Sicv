package com.tcc.sicv.utils

object Constants {
    //Key
    val PREF_KEY = "preferences"
    val MAINTENANCE_KEY = "manutencao"
    val TICKET_KEY = "ticket_maintenance"

    //Colection
    val USER_COLLECTION_PATH = "usuarios"
    val VEHICLES_COLLECTION_PATH = "veiculos"
    val MAINTENANCE_COLLECTION_PATH = "manutencao"
    val LOGS_COLLECTION_PATH = "logs"
    val TICKET_COLLECTION_PATH = "ticket"

    //Field
    val USER_FIELD = "user"
    val IMAGE_FIELD = "imagem"
    val MODEL_FIELD = "modelo"
    val POWER_FIELD = "potencia"
    val PRICE_FIELD = "preco"
    val MARK_FIELD = "marca"
    val TYPE_FIELD = "tipo"
    val SPEED_FIELD = "velocidade"
    val CODE_VEHICLE_FIELD = "cod_veiculo"
    val DATE_FIELD = "data"
    val DESCRIPTION_FIELD = "descricao"
    val RELEASE_VEHICLE_FIELD = "veiculo_liberado"
    val COST_FIELD = "gasto"
    val MAINTENANCE_FIELD = "manutencao"
    val COST_TICKET_FIELD = "custoTotal"
    val CODE_VEHICLE_TICKET_FIELD = "codigoVeiculo"
    val TIME_TICKET_FIELD = "time"
    val DATE_BUY_TICKET_FIELD = "dataAgendada"

    //Numbers
    val DATE_MIN_LENGHT = 10
    val USER_MIN_AGE = 18

    //Strings
    val DATE_FORMAT = "dd/MM/yyyy"
    val RESULT_TAG = "result_tag"
    val BUY_VEHICLE = "buy_vehicle"
    val MY_VEHICLES = "my_vehicles"
    val FROM_ACTIVITY = "from_activity"
}
