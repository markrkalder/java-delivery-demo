# java-delivery-demo

Sub-functionality of the food delivery application, which
calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weather
conditions.  

One available endpoint:  
/delivery  
  
required parameters:  
(String) city - tartu, tallinn, pärnu  
(String) vehicle - car, scooter, bike  

Database xml file import frequency configurable with cron expression in application.properties (weatherdata.cron value)
