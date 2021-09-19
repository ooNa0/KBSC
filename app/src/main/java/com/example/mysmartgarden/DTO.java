package com.example.mysmartgarden;

public class DTO {
    private String name;
    private String species;
    private String ip;
    private String deviceID;

    public DTO(){ }

    // 생성자 초기화
    public DTO(String deviceID,String name, String species, String ip) {
        this.deviceID=deviceID;
        this.name = name;
        this.species = species;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getIp(){ return ip; }

    public void setIp(String ip){ this.ip = ip; }

    public String getDeviceID(){return deviceID;}

    public void setDeviceID(String deviceID){this.deviceID=deviceID;}
}
