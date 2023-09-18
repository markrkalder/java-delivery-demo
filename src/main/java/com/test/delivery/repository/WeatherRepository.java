package com.test.delivery.repository;

import com.test.delivery.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    Weather findTopByOrderByIdDesc();
}
