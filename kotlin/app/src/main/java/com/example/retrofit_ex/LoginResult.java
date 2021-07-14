package com.example.retrofit_ex;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

    private String name;

    //@SerializedName("email")
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
