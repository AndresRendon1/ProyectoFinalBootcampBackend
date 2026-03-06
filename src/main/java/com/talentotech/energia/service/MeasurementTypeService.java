package com.talentotech.energia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.talentotech.energia.repository.MeasurementTypeRepository;
import com.talentotech.energia.model.MeasurementType;
import com.talentotech.energia.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementTypeService {

    private final MeasurementTypeRepository measurementTypeRepository;

    public MeasurementType save(MeasurementType measurementType) {

        if (measurementTypeRepository.existsByName(measurementType.getName())) {
            throw new ResourceNotFoundException("MeasurementType already exists");
        }

        return measurementTypeRepository.save(measurementType);
    }

    public List<MeasurementType> findAll() {
        return measurementTypeRepository.findAll();
    }

    public MeasurementType findById(Long id) {
        return measurementTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MeasurementType not found"));
    }

    public MeasurementType findByName(String name) {
        return measurementTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("MeasurementType not found"));
    }
}
