package com.ahmed.fynd;

public class OrderProducts {
    String product_name,order_id,quantity;

    public OrderProducts(String product_name, String order_id, String quantity) {
        this.product_name = product_name;
        this.order_id = order_id;
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
