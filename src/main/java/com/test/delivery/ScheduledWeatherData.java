package com.test.delivery;

import com.test.delivery.entity.Weather;
import com.test.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
@Component
public class ScheduledWeatherData {

    @Autowired
    private WeatherRepository weatherRepository;

    @Scheduled(cron = "${weatherdata.cron}")
    private void fetchWeatherData() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            document = factory.newDocumentBuilder().parse(new URI("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php").toURL().openStream());
            document.getDocumentElement().normalize();
        } catch (IOException | ParserConfigurationException | SAXException | URISyntaxException e){
            System.out.println("ERROR IN ScheduledWeatherData class: " + e.getMessage());
            return;
        }
        // I need to multiply the timestamp value by 1000 because the Timestamp object and SQL expects a Unix timestamp,
        // But the timestamp given by the XML file is a seconds-based Epoch value (instead of milliseconds).
        Timestamp timestamp = new Timestamp(Long.parseLong(document.getDocumentElement().getAttribute("timestamp")) * 1000);

        Map<String, String> cityMap = new HashMap<>() {{
            put("Tallinn-Harku", "tallinn");
            put("Tartu-Tõravere", "tartu");
            put("Pärnu", "pärnu");
        }};

        NodeList nodeList = document.getElementsByTagName("station");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element element = (Element) node;
            String name = element.getElementsByTagName("name").item(0).getTextContent();

            if (cityMap.containsKey(name)){
                int WMO = Integer.parseInt(element.getElementsByTagName("wmocode").item(0).getTextContent());
                float airtemperature = Float.parseFloat(element.getElementsByTagName("airtemperature").item(0).getTextContent());
                float windspeed = Float.parseFloat(element.getElementsByTagName("windspeed").item(0).getTextContent());
                String phenomenon = element.getElementsByTagName("phenomenon").item(0).getTextContent();

                weatherRepository.save(new Weather(
                        cityMap.get(name),
                        WMO,
                        airtemperature,
                        windspeed,
                        phenomenon,
                        timestamp
                ));
            }
        }
    }
}
