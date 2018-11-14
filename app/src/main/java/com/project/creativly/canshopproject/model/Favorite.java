package com.project.creativly.canshopproject.model;

import android.graphics.drawable.Drawable;

public class Favorite {
    private String id, price, date, title;
    private Drawable image;

    public Favorite(String id, String price, String date, String title, Drawable image) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
