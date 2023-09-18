package com.test.delivery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity
public class Weather {

    public Weather(){}

    /**
     * Constructor
     * @param name weather station name
     * @param WMO weather station WMO code
     * @param timestamp timestamp of when the data was imported
     */
    public Weather(String name, int WMO, float airTemperature, float windSpeed, String phenomenon, Timestamp timestamp) {
        this.name = name;
        this.WMO = WMO;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.phenomenon = phenomenon;
        this.timestamp = timestamp;
    }

    /**
     * Getter
     * @return air temperature value for the given database entry
     */
    public float getAirTemperature() {
        return airTemperature;
    }

    /**
     * Getter
     * @return wind speed value for the given database entry
     */
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * Getter
     * @return weather phenomenon value for the given database entry
     */
    public String getPhenomenon() {
        return phenomenon;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int WMO;
    private float airTemperature;
    private float windSpeed;
    private String phenomenon;
    Timestamp timestamp;


}
