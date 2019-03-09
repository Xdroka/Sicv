package com.tcc.sicv.data.model;

public class MaintenanceVehicle {
    private String maintenanceCode;
    private String modelo;
    private String imagem;
    private String cod_veiculo;
    private Boolean veiculo_liberado = false;

    public MaintenanceVehicle(
            String maintenanceCode,
            String modelo,
            String imagem,
            String cod_veiculo,
            Boolean veiculo_liberado
    ) {
        this.maintenanceCode = maintenanceCode;
        this.modelo = modelo;
        this.imagem = imagem;
        this.cod_veiculo = cod_veiculo;
        this.veiculo_liberado = veiculo_liberado;
    }

    public Boolean getVeiculo_liberado() { return veiculo_liberado; }

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
