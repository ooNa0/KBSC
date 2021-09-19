package com.example.mysmartgarden;

public class Singleton {
    private String name;
    private String ip;
    private String species;

    private Singleton(){

    }

    public static class LazyHolder{
        public static final Singleton instance = new Singleton();
    }

    //싱글톤 패턴 구현
    public static Singleton getInstance(){

        return  LazyHolder.instance;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getIp(){
        return ip;
    }

    public void setIp(String ip){
        this.ip=ip;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species){
        this.species=species;
    }


}
