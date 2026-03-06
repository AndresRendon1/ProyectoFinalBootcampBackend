package com.talentotech.energia.repository;

import com.talentotech.energia.model.EnergyRecord;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnergyRecordRepository extends JpaRepository<EnergyRecord, Long> {

    interface EnergyTypeMonthTotalProjection {
        Long getEnergyTypeId();

        String getEnergyTypeName();

        Integer getMonth();

        BigDecimal getTotal();
    }

    List<EnergyRecord> findByPowerPlantId(Long powerPlantId);

    List<EnergyRecord> findByMeasurementTypeId(Long measurementTypeId);

    @Query("""
            select
                et.id as energyTypeId,
                et.name as energyTypeName,
                er.month as month,
                sum(er.value) as total
            from EnergyRecord er
                join er.powerPlant pp
                join pp.energyType et
                join pp.region r
                join r.country c
            where (:year is null or er.year = :year)
              and (:countryId is null or c.id = :countryId)
              and (:regionId is null or r.id = :regionId)
            group by et.id, et.name, er.month
            order by et.name, er.month
            """)
    List<EnergyTypeMonthTotalProjection> sumByEnergyTypeAndMonth(
            @Param("year") Integer year,
            @Param("countryId") Long countryId,
            @Param("regionId") Long regionId);

    Optional<EnergyRecord> findByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
            Long powerPlantId, Integer year, Integer month, Long measurementTypeId);

    boolean existsByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
            Long powerPlantId, Integer year, Integer month, Long measurementTypeId);
}
