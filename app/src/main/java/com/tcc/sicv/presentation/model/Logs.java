package com.tcc.sicv.presentation.model;

public class Logs {
    private String date;
    private String description;
    private String cost;

    public Logs(String date, String description, String cost) {
        this.date = date;
        this.description = description;
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
    }
}
