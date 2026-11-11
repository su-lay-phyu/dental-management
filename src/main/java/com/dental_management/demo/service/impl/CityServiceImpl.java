package com.dental_management.demo.service.impl;

import com.dental_management.demo.model.entities.City;
import com.dental_management.demo.model.request.CityRequest;
import com.dental_management.demo.repository.CityRepository;
import com.dental_management.demo.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;


    // Get all cities
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    // Get city by ID
    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
    }

    // Create city
    public City createCity(CityRequest request) {
        City city = new City();
        city.setName(request.getName());

        return cityRepository.save(city);
    }

    // Update city
    public City updateCity(Long id, CityRequest request) {
        City city = getCityById(id);
        city.setName(request.getName());

        return cityRepository.save(city);
    }

    // Delete city
    public void deleteCity(Long id) {
        City city = getCityById(id);
        cityRepository.delete(city);
    }
}