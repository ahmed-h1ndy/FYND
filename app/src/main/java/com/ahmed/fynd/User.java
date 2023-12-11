package com.ahmed.fynd;

public class User {
    private String name,email, password, birthDate, recoveryQuestion, recoveryAnswer, admin;
    public User(String name, String email, String password, String birthDate, String recoveryQuestion, String recoveryAnswer,String admin){
        this.name=name;
        this.birthDate=birthDate;
        this.email=email;
        this.password=password;
        this.recoveryAnswer=recoveryAnswer;
        this.recoveryQuestion=recoveryQuestion;
        this.admin=admin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getRecoveryQuestion() {
        return recoveryQuestion;
    }

    public String getRecoveryAnswer() {
        return recoveryAnswer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setRecoveryQuestion(String recoveryQuestion) {
        this.recoveryQuestion = recoveryQuestion;
    }

    public void setRecoveryAnswer(String recoveryAnswer) {
        this.recoveryAnswer = recoveryAnswer;
    }
}
