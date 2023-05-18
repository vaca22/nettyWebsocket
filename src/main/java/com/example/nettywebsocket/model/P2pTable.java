package com.example.nettywebsocket.model;

public class P2pTable {

    public String phone;
    public String device;
    public String note;

    public int status=0;

    public P2pTable(String phone, String device, String note) {
        this.phone = phone;
        this.device = device;
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public String getDevice() {
        return device;
    }


    public String getNote() {
        return note;
    }

    public String getPhone() {
        return phone;
    }
}
