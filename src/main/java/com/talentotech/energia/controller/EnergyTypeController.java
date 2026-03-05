package com.talentotech.energia.controller;
import com.talentotech.energia.service.EnergyTypeService;
import com.talentotech.energia.model.EnergyType;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

 @RestController
@RequestMapping("/api/energy-type")
@RequiredArgsConstructor
public class EnergyTypeController {

    private final EnergyTypeService energyTypeService;

    @PostMapping
    public EnergyType create(@RequestBody EnergyType energyType) {
        return energyTypeService.save(energyType);
    }

    @GetMapping
    public List<EnergyType> findAll() {
        return energyTypeService.findAll();
    }

    @GetMapping("/{id}")
    public EnergyType findById(@PathVariable Long id) {
        return energyTypeService.findById(id);
    }

    @GetMapping("/renewable/{renewable}")
    public List<EnergyType> findByRenewable(@PathVariable Boolean renewable) {
        return energyTypeService.findByRenewable(renewable);
    }
}
