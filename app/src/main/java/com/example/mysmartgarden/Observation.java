package com.example.mysmartgarden;

public class Observation {
    private int year;
    private int month;
    private int day;
    private String info;
    private String name;
    private String state;

    public Observation(){}

    public Observation(int year,int month,int day, String info,String name,String state){
        this.year=year;
        this.month=month;
        this.day=day;
        this.info=info;
        this.name=name;
        this.state=state;
    }

    public String getInfo() {
        return info;
    }

    public String getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
