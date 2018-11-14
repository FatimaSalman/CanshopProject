package com.project.creativly.canshopproject.model;

import java.io.Serializable;

public class Offer implements Serializable {
    private String lang, id, name, price, image, date, description, stock_status;

    public Offer(String lang, String id, String name, String price, String image, String date,
                 String description,String stock_status) {
        this.lang = lang;
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
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

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
