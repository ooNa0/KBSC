package com.example.mysmartgarden;

public class State {

    private Float humidity;//대기습도
    private Float soilwater;//토양
    private Float temperature;//온도
    private int waterlevel;//물통

    public void State(Float humidity,Float soilwater, Float temperature, int waterlevel){
        this.humidity=humidity;
        this.soilwater=soilwater;
        this.temperature=temperature;
        this.waterlevel=waterlevel;
    }

    public Float getHumidity() {
        return humidity;
    }

    public Float getSoilwater() {
        return soilwater;
    }

    public Float getTemperature() {
        return temperature;
    }

    public int getWaterlevel() {
        return waterlevel;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public void setSoilwater(Float soilwater) {
        this.soilwater = soilwater;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public void setWaterlevel(int waterlevel) {
        this.waterlevel = waterlevel;
    }
}
