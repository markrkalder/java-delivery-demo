package com.test.delivery.service;

import com.test.delivery.entity.Weather;
import com.test.delivery.exceptions.ForbiddenVehicleTypeException;
import com.test.delivery.exceptions.UnsupportedCityException;
import com.test.delivery.exceptions.UnsupportedVehicleTypeException;
import com.test.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DeliveryService {

    @Autowired
    private WeatherRepository weatherRepository;

    private static Map<String, Map<String, Float>> getRBFMap() {
        Map<String, Map<String, Float>> rbfFees = new HashMap<>();

        Map<String, Float> tallinnFees = new HashMap<>();
        tallinnFees.put("car", 4f);
        tallinnFees.put("scooter", 3.5f);
        tallinnFees.put("bike", 3f);
        rbfFees.put("tallinn", tallinnFees);

        Map<String, Float> tartuFees = new HashMap<>();
        tartuFees.put("car", 3.5f);
        tartuFees.put("scooter", 3f);
        tartuFees.put("bike", 2.5f);
        rbfFees.put("tartu", tartuFees);

        Map<String, Float> parnuFees = new HashMap<>();
        parnuFees.put("car", 3f);
        parnuFees.put("scooter", 2.5f);
        parnuFees.put("bike", 2f);
        rbfFees.put("pärnu", parnuFees);
        return rbfFees;
    }

    private float getRBF(String city, String vehicle) throws UnsupportedCityException, UnsupportedVehicleTypeException{
        Map<String, Map<String, Float>> rbfFees = getRBFMap();
        Map<String, Float> cityMap = rbfFees.get(city);
        if (cityMap == null) throw new UnsupportedCityException("This city is not supported, please try again with another city.");
        Float regionalBaseFee = cityMap.get(vehicle);
        if (regionalBaseFee == null) throw new UnsupportedVehicleTypeException("This vehicle type is not supported," +
                    " please try again with another vehicle type.");
        return regionalBaseFee;
    }

    private float calculateATEF(float airTemp){
        if (airTemp < -10.0f) return 1f;
        else if (airTemp >= -10.0f && airTemp <= 0.0f) return 0.5f;
        return 0.0f;
    }

    private float calculateWSEF(float windSpeed) throws ForbiddenVehicleTypeException {
        if (windSpeed > 20.0f) throw new ForbiddenVehicleTypeException("Usage of selected vehicle type is forbidden");
        if (windSpeed <= 20.0f && windSpeed >= 10.0f) return 0.5f;
        return 0.0f;
    }

    private float calculateWPEF(String weatherPhenomenon) throws ForbiddenVehicleTypeException {
        List<String> errorConditions = Arrays.asList("glaze", "hail", "thunder");
        if (errorConditions.contains(weatherPhenomenon)) throw new ForbiddenVehicleTypeException("Usage of selected vehicle type is forbidden");
        List<String> rainConditions = Arrays.asList("light shower", "moderate shower", "heavy shower", "light rain", "moderate rain", "heavy rain");
        if (rainConditions.contains(weatherPhenomenon)) return 0.5f;
        List<String> snowConditions = Arrays.asList("light snow shower", "moderate snow shower", "heavy snow shower",
                "light sleet", "moderate sleet", "light snowfall", "moderate snowfall", "heavy snowfall");
        if (snowConditions.contains(weatherPhenomenon)) return 1.0f;
        return 0.0f;
    }

    /**
     *
     * @param city city where the delivery takes place
     * @param vehicle what type of vehicle is used for the delivery
     * @return the total delivery fee
     * @throws UnsupportedCityException if an incorrect city name is used for the request
     * @throws UnsupportedVehicleTypeException if an incorrect vehicle type is used for the request
     * @throws ForbiddenVehicleTypeException if using the requested vehicle type is forbidden due to weather conditions
     */
    public float calculateFees(String city, String vehicle) throws UnsupportedCityException, UnsupportedVehicleTypeException, ForbiddenVehicleTypeException {
        city = city.toLowerCase();
        vehicle = vehicle.toLowerCase();
        float regionalBaseFee = getRBF(city, vehicle);

        // A car does not have any extra fees.
        if (vehicle.equals("car")) return regionalBaseFee;

        Weather lastWeatherEntry = weatherRepository.findTopByOrderByIdDesc();

        float airTempEF = calculateATEF(lastWeatherEntry.getAirTemperature());
        float weatherPhenomenonEF = calculateWPEF(lastWeatherEntry.getPhenomenon().toLowerCase());

        // Not bike means that it has to  be a scooter, and wind speed is only for bikes.
        if (!vehicle.equals("bike")) return regionalBaseFee + airTempEF + weatherPhenomenonEF;
        float windSpeedEF = calculateWSEF(lastWeatherEntry.getWindSpeed());
        return regionalBaseFee + airTempEF + weatherPhenomenonEF + windSpeedEF;
    }
}
