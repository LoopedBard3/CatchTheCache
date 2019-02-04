package edu.iastate.cs309.jr2.catchthecacheandroid.models;

public class UserLoginRequest {
    private String Username, Password;

    public UserLoginRequest(String User, String Pass){
        Username = User;
        Password = Pass;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }
}
