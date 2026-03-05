package com.talentotech.energia.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.talentotech.energia.repository.RegionRepository;
import com.talentotech.energia.repository.CountryRepository;
import com.talentotech.energia.model.Region;
import com.talentotech.energia.model.Country;
import com.talentotech.energia.exception.ResourceNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public Region save(Region region) {

        Long countryId = region.getCountry().getId();

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        if (regionRepository.existsByNameAndCountryId(region.getName(), countryId)) {
            throw new ResourceNotFoundException("Region already exists in this country");
        }

        region.setCountry(country);

        return regionRepository.save(region);
    }

    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    public Region findById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));
    }

    public List<Region> findByCountry(Long countryId) {
        return regionRepository.findByCountryId(countryId);
    }
}

