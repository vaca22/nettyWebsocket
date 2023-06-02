package com.example.nettywebsocket.model;

public class P2pUser {

    public String phone;
    public String password;


    public P2pUser(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
