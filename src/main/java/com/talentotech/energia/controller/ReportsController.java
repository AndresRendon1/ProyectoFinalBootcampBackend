package com.talentotech.energia.controller;

import com.talentotech.energia.dto.EnergyTypeMonthlySeriesResponse;
import com.talentotech.energia.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/energy-type-monthly")
    public List<EnergyTypeMonthlySeriesResponse> energyTypeMonthly(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean renewable,
            @RequestParam(name = "country_id", required = false) Long countryId,
            @RequestParam(name = "region_id", required = false) Long regionId) {
        Integer effectiveYear = (year == null) ? Year.now().getValue() : year;
        return reportsService.energyTypeMonthlyTotals(effectiveYear, renewable, countryId, regionId);
    }
}
