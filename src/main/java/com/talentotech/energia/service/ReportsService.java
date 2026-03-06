package com.talentotech.energia.service;

import com.talentotech.energia.dto.EnergyTypeMonthlySeriesResponse;
import com.talentotech.energia.model.EnergyType;
import com.talentotech.energia.repository.EnergyRecordRepository;
import com.talentotech.energia.repository.EnergyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final EnergyRecordRepository energyRecordRepository;
    private final EnergyTypeRepository energyTypeRepository;

    public List<EnergyTypeMonthlySeriesResponse> energyTypeMonthlyTotals(Integer year, Long countryId, Long regionId) {
        List<EnergyRecordRepository.EnergyTypeMonthTotalProjection> rows =
                energyRecordRepository.sumByEnergyTypeAndMonth(year, countryId, regionId);

        Map<Long, EnergyTypeMonthlySeriesResponse> seriesByEnergyTypeId = new HashMap<>();

        for (EnergyRecordRepository.EnergyTypeMonthTotalProjection row : rows) {
            seriesByEnergyTypeId.computeIfAbsent(row.getEnergyTypeId(), id -> {
                List<BigDecimal> data = new ArrayList<>(12);
                for (int i = 0; i < 12; i++) {
                    data.add(BigDecimal.ZERO);
                }
                return new EnergyTypeMonthlySeriesResponse(row.getEnergyTypeName(), data);
            });

            Integer month = row.getMonth();
            if (month != null && month >= 1 && month <= 12) {
                BigDecimal total = row.getTotal() == null ? BigDecimal.ZERO : row.getTotal();
                seriesByEnergyTypeId.get(row.getEnergyTypeId()).getData().set(month - 1, total);
            }
        }

        List<EnergyTypeMonthlySeriesResponse> result = new ArrayList<>();

        for (EnergyType energyType : energyTypeRepository.findAll()) {
            EnergyTypeMonthlySeriesResponse series = seriesByEnergyTypeId.get(energyType.getId());
            if (series == null) {
                List<BigDecimal> data = new ArrayList<>(12);
                for (int i = 0; i < 12; i++) {
                    data.add(BigDecimal.ZERO);
                }
                series = new EnergyTypeMonthlySeriesResponse(energyType.getName(), data);
            }
            result.add(series);
        }

        return result;
    }
}
