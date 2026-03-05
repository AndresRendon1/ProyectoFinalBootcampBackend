package com.talentotech.energia.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.talentotech.energia.repository.*;
import com.talentotech.energia.model.*;
import com.talentotech.energia.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerPlantService {

    private final PowerPlantRepository powerPlantRepository;
    private final CompanyRepository companyRepository;
    private final RegionRepository regionRepository;
    private final EnergyTypeRepository energyTypeRepository;

    public PowerPlant save(PowerPlant plant) {

        Long companyId = plant.getCompany().getId();
        Long regionId = plant.getRegion().getId();
        Long energyTypeId = plant.getEnergyType().getId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));

        EnergyType energyType = energyTypeRepository.findById(energyTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("EnergyType not found"));

        if (powerPlantRepository.existsByNameAndCompanyId(plant.getName(), companyId)) {
            throw new ResourceNotFoundException("PowerPlant already exists in this company");
        }

        plant.setCompany(company);
        plant.setRegion(region);
        plant.setEnergyType(energyType);

        return powerPlantRepository.save(plant);
    }

    public List<PowerPlant> findAll() {
        return powerPlantRepository.findAll();
    }

    public PowerPlant findById(Long id) {
        return powerPlantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PowerPlant not found"));
    }

    public List<PowerPlant> findByCompany(Long companyId) {
        return powerPlantRepository.findByCompanyId(companyId);
    }

    public List<PowerPlant> findByRegion(Long regionId) {
        return powerPlantRepository.findByRegionId(regionId);
    }
}
