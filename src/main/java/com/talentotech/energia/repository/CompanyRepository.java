package com.talentotech.energia.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talentotech.energia.model.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Buscar empresas por país
    List<Company> findByCountryId(Long countryId);

    // Validar empresa única por país
    Optional<Company> findByNameAndCountryId(String name, Long countryId);

    boolean existsByNameAndCountryId(String name, Long countryId);
}