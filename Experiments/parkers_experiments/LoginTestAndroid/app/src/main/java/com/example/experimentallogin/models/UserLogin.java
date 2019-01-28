package com.example.experimentallogin.models;

import java.io.Serializable;

public class UserLogin implements Serializable {
    private String username, password;

    public UserLogin(String name, String pass){
        username = name;
        password = pass;
    }
}
