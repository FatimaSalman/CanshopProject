package com.project.creativly.canshopproject.model;

import java.io.Serializable;

public class Sales implements Serializable {
    private String id, image, price, name, type, lang, date, description, stock_status;

    public Sales(String id, String image, String price, String name, String lang, String date, String description, String stock_status) {
        this.id = id;
        this.image = image;
        this.price = price;
        this.name = name;
        this.lang = lang;
        this.date = date;
        this.description = description;
        this.stock_status = stock_status;
    }

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
