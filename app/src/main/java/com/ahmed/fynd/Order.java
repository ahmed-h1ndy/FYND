package com.ahmed.fynd;
public class Order {
    int id;
    String price,rating,feedback,userEmail;

    public Order(int id, String price, String rating, String feedback, String userEmail) {
        this.id = id;
        this.price = price;
        this.rating = rating;
        this.feedback = feedback;
        this.userEmail = userEmail;
    }

    public int getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }


    public String getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
