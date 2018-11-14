package com.project.creativly.canshopproject.model;

public class Order {
    private String id, date, status_ar, status_en;

    public Order() {
    }

    public Order(String id, String date, String status_ar, String status_en) {
        this.id = id;
        this.date = date;
        this.status_ar = status_ar;
        this.status_en = status_en;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus_ar() {
        return status_ar;
    }

    public void setStatus_ar(String status_ar) {
        this.status_ar = status_ar;
    }

    public String getStatus_en() {
        return status_en;
    }

    public void setStatus_en(String status_en) {
        this.status_en = status_en;
    }
}
