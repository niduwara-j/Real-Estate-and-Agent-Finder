package com.dilshan.realestate.service;

import com.dilshan.realestate.dsa.SortEngine;
import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.Property;
import com.dilshan.realestate.model.enums.PropertyType;
import com.dilshan.realestate.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> findByAgent(Agent agent) {
        return propertyRepository.findByAgent(agent);
    }

    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    public List<Property> searchAndFilter(String keyword, PropertyType type, Double minPrice, Double maxPrice, String sortBy) {
        List<Property> properties = propertyRepository.searchProperties(
                (keyword != null && !keyword.isBlank()) ? keyword.trim() : null,
                type,
                minPrice,
                maxPrice
        );

        // Apply DSA Sorting based on request
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            return SortEngine.bubbleSortByPrice(properties, true);
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            return SortEngine.bubbleSortByPrice(properties, false);
        }

        return properties;
    }

    public long countTotalProperties() {
        return propertyRepository.count();
    }
}
