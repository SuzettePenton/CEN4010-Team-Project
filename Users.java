package com.example.profile_management.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@JsonPropertyOrder({ "username", "userPassword", "name", "email", "homeAddress" })
public class Users {
    @Id
    @Column(name = "Username")
    private String username;
    @Column(name = "User_Password")
    private String userPassword;
    @Column(name = "User_Name")
    private String name;
    @Column(name = "Email")
    private String email;
    @Column(name = "Home_Address")
    private String homeAddress;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
     public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

      public String getHomeAddress() {
        return homeAddress;
    }
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}