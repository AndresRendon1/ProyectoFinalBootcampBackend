package com.talentotech.energia.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.talentotech.energia.service.PowerPlantService;
import com.talentotech.energia.model.PowerPlant;
import java.util.List;

@RestController
@RequestMapping("/api/power-plant")
@RequiredArgsConstructor
public class PowerPlantController {

    private final PowerPlantService powerPlantService;

    @PostMapping
    public PowerPlant create(@RequestBody PowerPlant plant) {
        return powerPlantService.save(plant);
    }

    @GetMapping
    public List<PowerPlant> findAll() {
        return powerPlantService.findAll();
    }

    @GetMapping("/{id}")
    public PowerPlant findById(@PathVariable Long id) {
        return powerPlantService.findById(id);
    }

    @GetMapping("/company/{companyId}")
    public List<PowerPlant> findByCompany(@PathVariable Long companyId) {
        return powerPlantService.findByCompany(companyId);
    }

    @GetMapping("/region/{regionId}")
    public List<PowerPlant> findByRegion(@PathVariable Long regionId) {
        return powerPlantService.findByRegion(regionId);
    }
}