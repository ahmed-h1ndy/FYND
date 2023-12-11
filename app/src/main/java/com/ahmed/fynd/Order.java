package com.ahmed.fynd;
public class Order {
    String id,price,location,rating,feedback,userEmail;

    public Order(String id, String price, String location, String rating, String feedback, String userEmail) {
        this.id = id;
        this.price = price;
        this.location = location;
        this.rating = rating;
        this.feedback = feedback;
        this.userEmail = userEmail;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
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
