package com.talentotech.energia.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.talentotech.energia.model.Country;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    // Buscar por nombre
    Optional<Country> findByName(String name);

    // Validar si existe por nombre
    boolean existsByName(String name);
}