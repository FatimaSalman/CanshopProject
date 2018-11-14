package com.project.creativly.canshopproject.model;

import android.graphics.drawable.Drawable;

public class Cart {
    private String id, price, date, title, quantity, image;
    private String item_id, product_id, total;

    public Cart(String price, String title, String quantity, String item_id, String product_id, String total, String data) {
        this.price = price;
        this.title = title;
        this.quantity = quantity;
        this.item_id = item_id;
        this.product_id = product_id;
        this.total = total;
    }

    public Cart(String id, String image, String price, String quantity, String date, String title) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.title = title;
        this.image = image;
        this.quantity = quantity;
    }

    public Cart() {

    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
