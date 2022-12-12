package com.example.myapplication;

public class todolistItem {
    String todolist;
    String memo;
    int ID;
    int year;
    int month;
    int day;

    public todolistItem(String todolist, String memo, int id, int year, int month, int day) {
        this.todolist = todolist;
        this.memo = memo;
        this.ID = id;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public String gettodolist() {
        return todolist;
    }
    public void settodolist (String name) {
        this.todolist = todolist;
    }

    public String getmemo(){
        return memo;
    }
    public void setmemo(String memo){
        this.memo=memo;
    }
    public int getId(){
        return ID;
    }
    public void setID(int ID){
        this.ID=ID;
    }

    public int getYear(){return year;}
    public void setYear(int year){this.year=year;}
    public int getMonth(){return month;}
    public void setMonth(int month){this.month=month;}
    public int getDay(){return day;}
    public void setDay(int day){this.day=day;}
}
