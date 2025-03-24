package com.example.yourmealapp.models;

public class User {
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private String role;

    public User(String username, String password, String fullname, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
}
