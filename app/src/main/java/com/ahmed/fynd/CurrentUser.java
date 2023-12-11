package com.ahmed.fynd;

public class CurrentUser {
    String email,remember;
    public CurrentUser(String email, String remember){
        this.email=email;
        this.remember=remember;
    }

    public String getEmail() {
        return email;
    }

    public String getRemember() {
        return remember;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }
}
