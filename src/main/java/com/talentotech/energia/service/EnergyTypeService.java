package com.talentotech.energia.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.talentotech.energia.repository.EnergyTypeRepository;
import com.talentotech.energia.model.EnergyType;
import com.talentotech.energia.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnergyTypeService {

    private final EnergyTypeRepository energyTypeRepository;

    public EnergyType save(EnergyType energyType) {

        if (energyTypeRepository.existsByName(energyType.getName())) {
            throw new ResourceNotFoundException("EnergyType already exists");
        }

        return energyTypeRepository.save(energyType);
    }

    public List<EnergyType> findAll() {
        return energyTypeRepository.findAll();
    }

    public EnergyType findById(Long id) {
        return energyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EnergyType not found"));
    }

    public List<EnergyType> findByRenewable(Boolean renewable) {
        return energyTypeRepository.findByRenewable(renewable);
    }
}
