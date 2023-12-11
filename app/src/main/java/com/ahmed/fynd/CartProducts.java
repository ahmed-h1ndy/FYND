package com.ahmed.fynd;

public class CartProducts {
    String product_name,userEmail,quantity;

    public CartProducts(String product_name, String userEmail, String quantity) {
        this.product_name = product_name;
        this.userEmail = userEmail;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
