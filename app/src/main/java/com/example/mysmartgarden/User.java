package com.example.mysmartgarden;

import androidx.work.Data;


public class User {

    private String ip;
    private String name;
    private String species;

    public void setIp(String ip){
        this.ip=ip;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setSpecies(String species){
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

