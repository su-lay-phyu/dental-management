package com.dental_management.demo.service;

import com.dental_management.demo.model.entities.City;
import com.dental_management.demo.model.request.CityRequest;

import java.util.List;

public interface CityService {

    public List<City> getAllCities();
    public City getCityById(Long id);
    public City createCity(CityRequest request);
    public City updateCity(Long id, CityRequest request);
    public void deleteCity(Long id);
}
