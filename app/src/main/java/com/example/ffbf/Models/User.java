package com.example.ffbf.Models;

public class User {

    public String fullName, age, email, id, userType, user_name, avatar;

    public User(){

    }

    public User(String fullName, String age, String email, String id, String userType, String user_name, String avatar) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.id = id;
        this.userType = userType;
        this.user_name = user_name;
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
