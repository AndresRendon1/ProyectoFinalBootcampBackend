package com.talentotech.energia.config;

import com.talentotech.energia.model.Company;
import com.talentotech.energia.model.Country;
import com.talentotech.energia.model.EnergyRecord;
import com.talentotech.energia.model.EnergyType;
import com.talentotech.energia.model.MeasurementType;
import com.talentotech.energia.model.PowerPlant;
import com.talentotech.energia.model.Region;
import com.talentotech.energia.repository.CompanyRepository;
import com.talentotech.energia.repository.CountryRepository;
import com.talentotech.energia.repository.EnergyRecordRepository;
import com.talentotech.energia.repository.EnergyTypeRepository;
import com.talentotech.energia.repository.MeasurementTypeRepository;
import com.talentotech.energia.repository.PowerPlantRepository;
import com.talentotech.energia.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final CompanyRepository companyRepository;
    private final EnergyTypeRepository energyTypeRepository;
    private final MeasurementTypeRepository measurementTypeRepository;
    private final PowerPlantRepository powerPlantRepository;
    private final EnergyRecordRepository energyRecordRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Country colombia = seedCountry("Colombia");
        Country mexico = seedCountry("Mexico");

        Region antioquia = seedRegion("Antioquia", colombia);
        Region cundinamarca = seedRegion("Cundinamarca", colombia);
        Region jalisco = seedRegion("Jalisco", mexico);

        Company ePM = seedCompany("EPM", colombia);
        Company cfe = seedCompany("CFE", mexico);

        EnergyType solar = seedEnergyType("Solar", true);
        EnergyType coal = seedEnergyType("Coal", false);
        EnergyType wind = seedEnergyType("Wind", true);

        MeasurementType generated = seedMeasurementType("Generated Energy", "kWh");

        PowerPlant guatape = seedPowerPlant("Guatape Plant", ePM, antioquia, solar);
        PowerPlant zipaquira = seedPowerPlant("Zipaquira Plant", ePM, cundinamarca, coal);
        PowerPlant guadalajara = seedPowerPlant("Guadalajara Plant", cfe, jalisco, solar);
        PowerPlant medellinWind = seedPowerPlant("Medellin Wind Farm", ePM, antioquia, wind);

        // seed records for each month of 2026 for the three existing plants
        for (int m = 1; m <= 12; m++) {
            seedEnergyRecord(guatape, generated, 2026, m, new BigDecimal(11000 + m * 300 + ".00"));
            seedEnergyRecord(zipaquira, generated, 2026, m, new BigDecimal(9000 + m * 200 + ".00"));
            seedEnergyRecord(guadalajara, generated, 2026, m, new BigDecimal(7000 + m * 150 + ".00"));
            seedEnergyRecord(medellinWind, generated, 2026, m, new BigDecimal(8000 + m * 180 + ".00"));
        }

        // additional country/region/company/plant for other part of the world
        Country usa = seedCountry("USA");
        Region california = seedRegion("California", usa);
        Company pge = seedCompany("PG&E", usa);
        PowerPlant sanJose = seedPowerPlant("San Jose Plant", pge, california, solar);
        for (int m = 1; m <= 12; m++) {
            seedEnergyRecord(sanJose, generated, 2026, m, new BigDecimal(5000 + m * 100 + ".00"));
        }

        // another example for Europe
        Country spain = seedCountry("Spain");
        Region catalonia = seedRegion("Catalonia", spain);
        Company iberdrola = seedCompany("Iberdrola", spain);
        PowerPlant barcelona = seedPowerPlant("Barcelona Plant", iberdrola, catalonia, solar);
        for (int m = 1; m <= 12; m++) {
            seedEnergyRecord(barcelona, generated, 2026, m, new BigDecimal(6000 + m * 120 + ".00"));
        }
    }

    private Country seedCountry(String name) {
        return countryRepository.findByName(name)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setName(name);
                    return countryRepository.save(c);
                });
    }

    private Region seedRegion(String name, Country country) {
        return regionRepository.findByNameAndCountryId(name, country.getId())
                .orElseGet(() -> {
                    Region r = new Region();
                    r.setName(name);
                    r.setCountry(country);
                    return regionRepository.save(r);
                });
    }

    private Company seedCompany(String name, Country country) {
        return companyRepository.findByNameAndCountryId(name, country.getId())
                .orElseGet(() -> {
                    Company c = new Company();
                    c.setName(name);
                    c.setCountry(country);
                    return companyRepository.save(c);
                });
    }

    private EnergyType seedEnergyType(String name, boolean renewable) {
        if (energyTypeRepository.existsByName(name)) {
            return energyTypeRepository.findByName(name).orElseThrow();
        }
        EnergyType t = new EnergyType();
        t.setName(name);
        t.setRenewable(renewable);
        return energyTypeRepository.save(t);
    }

    private MeasurementType seedMeasurementType(String name, String unit) {
        return measurementTypeRepository.findByName(name)
                .orElseGet(() -> {
                    MeasurementType m = new MeasurementType();
                    m.setName(name);
                    m.setUnit(unit);
                    return measurementTypeRepository.save(m);
                });
    }

    private PowerPlant seedPowerPlant(String name, Company company, Region region, EnergyType energyType) {
        if (powerPlantRepository.existsByNameAndCompanyId(name, company.getId())) {
            return powerPlantRepository.findByNameAndCompanyId(name, company.getId()).orElseThrow();
        }
        PowerPlant p = new PowerPlant();
        p.setName(name);
        p.setCompany(company);
        p.setRegion(region);
        p.setEnergyType(energyType);
        return powerPlantRepository.save(p);
    }

    private EnergyRecord seedEnergyRecord(PowerPlant plant, MeasurementType measurementType, Integer year, Integer month, BigDecimal value) {
        if (energyRecordRepository.existsByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
                plant.getId(), year, month, measurementType.getId())) {
            return energyRecordRepository.findByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
                    plant.getId(), year, month, measurementType.getId()).orElseThrow();
        }

        EnergyRecord r = new EnergyRecord();
        r.setPowerPlant(plant);
        r.setMeasurementType(measurementType);
        r.setYear(year);
        r.setMonth(month);
        r.setValue(value);
        return energyRecordRepository.save(r);
    }
}
