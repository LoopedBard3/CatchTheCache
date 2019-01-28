package com.example.experimentallogin.models;

import java.io.Serializable;

public class UserLoginResponse implements Serializable {
    private boolean success, new_user;

    public UserLoginResponse(boolean suc, boolean new_usr){
        success = suc;
        new_user = new_usr;
    }
}
