package com.test.delivery;

import com.test.delivery.entity.Weather;
import com.test.delivery.exceptions.ForbiddenVehicleTypeException;
import com.test.delivery.exceptions.UnsupportedCityException;
import com.test.delivery.exceptions.UnsupportedVehicleTypeException;
import com.test.delivery.repository.WeatherRepository;
import com.test.delivery.service.DeliveryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DeliveryApplicationTests {
	@InjectMocks
	private DeliveryService deliveryService = new DeliveryService();

	@Mock
	private WeatherRepository weatherRepository;
	// With cars I'll also test that no extra fees nor weather phenomenon errors are applied.
	@Test
	void tartuCarTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 15f, "thunder", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "car") == 3.5);
	}

	@Test
	void tallinnCarTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tallinn", 1111, 12.5f, 23f, "light rain", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tallinn", "car") == 4);
	}

	@Test
	void parnuCarTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("pärnu", 1111, -30f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("pärnu", "car") == 3);
	}

	@Test
	void tartuBikeTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "bike") == 2.5);
	}

	@Test
	void tallinnBikeTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tallinn", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tallinn", "bike") == 3);
	}

	@Test
	void parnuBikeTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("pärnu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("pärnu", "bike") == 2);
	}

	@Test
	void tartuScooterTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "scooter") == 3);
	}

	@Test
	void tallinnScooterTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tallinn", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tallinn", "scooter") == 3.5);
	}

	@Test
	void parnuScooterTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("pärnu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("pärnu", "scooter") == 2.5);
	}

	@Test
	void bikeRainEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "light rain", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "bike") == 3);
	}

	@Test
	void bikeSnowEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 1.5f, 5.3f, "light snowfall", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "bike") == 3.5);
	}

	@Test
	void scooterRainEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "light rain", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "scooter") == 3.5);
	}

	@Test
	void scooterSnowEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 1.5f, 5.3f, "light snowfall", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "scooter") == 4);
	}

	@Test
	void bikeWindSpeedEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 13.3f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "bike") == 3);
	}

	@Test
	void scooterWindSpeedNoEFTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 12f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "scooter") == 3);
	}

	@Test
	void scooterWindSpeedNoErrorTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 22f, "", new Timestamp(1695054067L * 1000)));
		assert(deliveryService.calculateFees("tartu", "scooter") == 3);
	}

	@Test
	void bikeWindSpeedErrorTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 25f, "", new Timestamp(1695054067L * 1000)));
		assertThrows(ForbiddenVehicleTypeException.class, () -> deliveryService.calculateFees("tartu", "bike"));
	}

	@Test
	void scooterWeatherPhenomenonErrorTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "thunder", new Timestamp(1695054067L * 1000)));
		assertThrows(ForbiddenVehicleTypeException.class, () -> deliveryService.calculateFees("tartu", "scooter"));
	}

	@Test
	void bikeWeatherPhenomenonErrorTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "thunder", new Timestamp(1695054067L * 1000)));
		assertThrows(ForbiddenVehicleTypeException.class, () -> deliveryService.calculateFees("tartu", "bike"));
	}

	@Test
	void unsupportedVehicleTypeTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assertThrows(UnsupportedVehicleTypeException.class, () -> deliveryService.calculateFees("tartu", "boat"));
	}

	@Test
	void unsupportedCityTest() {
		Mockito.when(weatherRepository.findTopByOrderByIdDesc()).thenReturn(new Weather("tartu", 1111, 12.5f, 5.3f, "", new Timestamp(1695054067L * 1000)));
		assertThrows(UnsupportedCityException.class, () -> deliveryService.calculateFees("tapa", "car"));
	}

}
