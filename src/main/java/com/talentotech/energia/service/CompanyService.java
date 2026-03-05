package com.talentotech.energia.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.talentotech.energia.repository.CountryRepository;
import com.talentotech.energia.repository.CompanyRepository;
import com.talentotech.energia.model.Country;
import com.talentotech.energia.model.Company;
import com.talentotech.energia.exception.ResourceNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CountryRepository countryRepository;

    public Company save(Company company) {

        Long countryId = company.getCountry().getId();

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found"));

        if (companyRepository.existsByNameAndCountryId(company.getName(), countryId)) {
            throw new ResourceNotFoundException("Company already exists in this country");
        }

        company.setCountry(country);

        return companyRepository.save(company);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public List<Company> findByCountry(Long countryId) {
        return companyRepository.findByCountryId(countryId);
    }
}
