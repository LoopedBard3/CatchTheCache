package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int authority;


    public User(String username, int authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public int getAuthority() {
        return authority;
    }
}
