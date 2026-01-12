package com.example.menu1;

public class User {
    String name;
    String password;
    String nickname;
    String surname;

    public User(String name, String password, String nickname, String surname) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
