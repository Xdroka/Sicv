package com.tcc.sicv.presentation.model;

import java.util.List;

public class MaintenanceVehicle {
    private String maintenanceCode;
    private String modelVehicle;
    private String vehiclePhotoUrl;
    private String vehicleCode;
    private List<Logs> logs;

    public MaintenanceVehicle(String maintenanceCode, String modelVehicle, String vehiclePhoto, String vehicleCode, List<Logs> logs) {
        this.maintenanceCode = maintenanceCode;
        this.modelVehicle = modelVehicle;
        this.vehiclePhotoUrl = vehiclePhoto;
        this.vehicleCode = vehicleCode;
        this.logs = logs;
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

    public List<Logs> getLogs() {
        return logs;
    }

    public void setLogs(List<Logs> logs) {
        this.logs = logs;
    }
}
