package com.example.myapplication;

public class PhoneBookShowItem {
    int id;
    String name;
    String mobile;
    int age;


    public PhoneBookShowItem(int id, String name, String mobile,int age) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.age = age;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge(){return age;}

    public void setAge(int age){this.age = age;}
}