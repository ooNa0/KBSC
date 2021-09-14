package com.example.mysmartgarden;

import androidx.work.Data;


public class User {

    private String ip;
    private String name;
    private String species;

    public User(){}

    public User(String name,String ip,String Species){
        this.ip=ip;
        this.name=name;
        this.species=species;
    }



    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }
}

