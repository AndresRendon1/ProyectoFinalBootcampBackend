package com.talentotech.energia.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.talentotech.energia.service.RegionService;
import com.talentotech.energia.model.Region;

import java.util.List;

@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    public Region create(@RequestBody Region region) {
        return regionService.save(region);
    }

    @GetMapping
    public List<Region> findAll() {
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public Region findById(@PathVariable Long id) {
        return regionService.findById(id);
    }

    @GetMapping("/country/{countryId}")
    public List<Region> findByCountry(@PathVariable Long countryId) {
        return regionService.findByCountry(countryId);
    }
}
