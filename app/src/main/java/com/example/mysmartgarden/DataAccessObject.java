package com.example.mysmartgarden;

public class DataAccessObject {
    private String name;
    private String species;

    // 생성자 초기화
    public DataAccessObject(String name, String species) {
        this.name = name;
        this.species = species;
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

    public void setSpecies(String data) {
        this.species = species;
    }

}
