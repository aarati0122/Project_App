package com.mobile.project1;

public class Database {
    private String Fullname;
    private String Email;
    private  String Password;
    private String Address;
    private String Mobile;

    public Database(String fullname, String email, String password, String address, String mobile) {
        Fullname = fullname;
        Email = email;
        Password = password;
        Address = address;
        Mobile = mobile;
    }


    public Database() {
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
