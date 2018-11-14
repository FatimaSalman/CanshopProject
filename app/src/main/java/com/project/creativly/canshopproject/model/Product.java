package com.project.creativly.canshopproject.model;

import android.graphics.drawable.Drawable;

public class Product {
    private String id, price, total, title, quantity;

    public Product(String id, String price, String total, String title, String quantity) {
        this.id = id;
        this.price = price;
        this.total = total;
        this.title = title;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
