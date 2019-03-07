package com.tcc.sicv.data.model;

public class MaintenanceVehicle {
    private String maintenanceCode;
    private String modelo;
    private String imagem;
    private String cod_veiculo;
    private String total_gasto;

    public MaintenanceVehicle(String maintenanceCode, String modelo, String imagem, String cod_veiculo, String total_gasto) {
        this.maintenanceCode = maintenanceCode;
        this.modelo = modelo;
        this.imagem = imagem;
        this.cod_veiculo = cod_veiculo;
        this.total_gasto = total_gasto;
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

    public String getTotal_gasto() {
        return total_gasto;
    }

    public void setTotal_gasto(String total_gasto) {
        this.total_gasto = total_gasto;
    }
}
