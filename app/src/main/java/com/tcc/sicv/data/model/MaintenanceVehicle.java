package com.tcc.sicv.data.model;

public class MaintenanceVehicle {
    private String maintenanceCode;
    private String modelVehicle;
    private String vehiclePhotoUrl;
    private String vehicleCode;
    private String totalCost;

    public MaintenanceVehicle(String maintenanceCode, String modelVehicle, String vehiclePhotoUrl, String vehicleCode, String totalCost) {
        this.maintenanceCode = maintenanceCode;
        this.modelVehicle = modelVehicle;
        this.vehiclePhotoUrl = vehiclePhotoUrl;
        this.vehicleCode = vehicleCode;
        this.totalCost = totalCost;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getMaintenanceCode() {
        return maintenanceCode;
    }

    public String getModelVehicle() {
        return modelVehicle;
    }

    public String getVehiclePhotoUrl() {
        return vehiclePhotoUrl;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
