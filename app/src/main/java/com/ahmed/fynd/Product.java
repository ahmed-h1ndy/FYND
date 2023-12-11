package com.ahmed.fynd;

public class Product {
    String name,category,price,quantity,sales;
    byte[] image;

    public Product(String name, String category, String price, String quantity, byte[] image, String sales) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public byte[] getImage() {
        return image;
    }

    public String getSales() {
        return sales;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }
}
