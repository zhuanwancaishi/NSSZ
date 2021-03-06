package com.levent_j.nssz.entry;

/**
 * Created by levent_j on 16-5-6.
 */
public class Device {


    private int deviceNumber;
    private int Temperature;
    private int temperatureDecimal;
    private int humidity;
    private int state;

    public Device(){
        this.deviceNumber=1;
        this.Temperature=0;
        this.temperatureDecimal=0;
        this.humidity=0;
        this.state=0;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(int deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getTemperature() {
        return Temperature;
    }

    public void setTemperature(int temperature) {
        Temperature = temperature;
    }

    public int getTemperatureDecimal() {
        return temperatureDecimal;
    }

    public void setTemperatureDecimal(int temperatureDecimal) {
        this.temperatureDecimal = temperatureDecimal;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
