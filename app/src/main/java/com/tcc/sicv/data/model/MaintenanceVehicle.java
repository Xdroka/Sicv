package com.tcc.sicv.data.model;

public class MaintenanceVehicle {
    private String maintenanceCode;
    private String modelo;
    private String imagem;
    private String cod_veiculo;

    public MaintenanceVehicle(String maintenanceCode, String modelo, String imagem, String cod_veiculo) {
        this.maintenanceCode = maintenanceCode;
        this.modelo = modelo;
        this.imagem = imagem;
        this.cod_veiculo = cod_veiculo;
    }

    public String getCod_veiculo() {
        return cod_veiculo;
    }

    public String getMaintenanceCode() {
        return maintenanceCode;
    }

    public String getModelo() {
        return modelo;
    }

    public String getImagem() {
        return imagem;
    }

}
