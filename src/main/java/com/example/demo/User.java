package com.example.demo;


import lombok.Data;

@Data
public class User {


    private String name;
    private int age;

    public static void main(String[] args){

        User user = new User();

        user.setAge(11);
        user.setName("aaaa");

    }
}

