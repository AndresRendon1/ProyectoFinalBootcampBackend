package com.talentotech.energia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.talentotech.energia.repository.EnergyRecordRepository;
import com.talentotech.energia.repository.PowerPlantRepository;
import com.talentotech.energia.repository.MeasurementTypeRepository;
import com.talentotech.energia.model.EnergyRecord;
import com.talentotech.energia.model.PowerPlant;
import com.talentotech.energia.model.MeasurementType;
import com.talentotech.energia.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnergyRecordService {

    private final EnergyRecordRepository energyRecordRepository;
    private final PowerPlantRepository powerPlantRepository;
    private final MeasurementTypeRepository measurementTypeRepository;

    public EnergyRecord save(EnergyRecord record) {

        if (record == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EnergyRecord payload is required");
        }

        if (record.getYear() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "year is required");
        }

        if (record.getMonth() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "month is required");
        }

        if (record.getValue() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "value is required");
        }

        if (record.getPowerPlant() == null || record.getPowerPlant().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "powerPlant.id is required");
        }

        if (record.getMeasurementType() == null || record.getMeasurementType().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "measurementType.id is required");
        }

        Long plantId = record.getPowerPlant().getId();
        Long measurementTypeId = record.getMeasurementType().getId();

        PowerPlant powerPlant = powerPlantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("PowerPlant not found"));

        MeasurementType measurementType = measurementTypeRepository.findById(measurementTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("MeasurementType not found"));

        if (energyRecordRepository.existsByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
                plantId, record.getYear(), record.getMonth(), measurementTypeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EnergyRecord already exists for this plant/year/month/measurementType");
        }

        record.setPowerPlant(powerPlant);
        record.setMeasurementType(measurementType);

        return energyRecordRepository.save(record);
    }

    public List<EnergyRecord> findAll() {
        return energyRecordRepository.findAll();
    }

    public EnergyRecord findById(Long id) {
        return energyRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EnergyRecord not found"));
    }

    public List<EnergyRecord> findByPlant(Long plantId) {
        return energyRecordRepository.findByPowerPlantId(plantId);
    }
}
