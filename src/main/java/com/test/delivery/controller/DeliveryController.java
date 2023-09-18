package com.test.delivery.controller;

import com.test.delivery.exceptions.ForbiddenVehicleTypeException;
import com.test.delivery.exceptions.UnsupportedCityException;
import com.test.delivery.exceptions.UnsupportedVehicleTypeException;
import com.test.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    /**
     *
     * @param city city where the delivery takes place
     * @param vehicle what type of vehicle is used for the delivery
     * @return the total delivery fee
     */
    @GetMapping("/delivery")
    // Leaving the return type as string and returning a descriptive phrase too, although it wouldn't be optimal
    // If the endpoint is being used by another program.
    public ResponseEntity<String> deliveryFee(
            // I didn't convert them to classes, because I think it is simpler and more effective
            // as strings instead of classes with one propetry, constructor and getter.
            @RequestParam String city,
            @RequestParam String vehicle
    ) {
        try {
            float deliveryFee = deliveryService.calculateFees(city, vehicle);
            return ResponseEntity.ok("Delivery fee: " + deliveryFee);
        } catch (UnsupportedCityException | UnsupportedVehicleTypeException | ForbiddenVehicleTypeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        // If something else should happen then I will log it in the console because more advanced logging is not implemented.
        catch (Exception e) {
            System.out.println("UNEXPECTED ERROR IN CONTROLLER: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }
}
