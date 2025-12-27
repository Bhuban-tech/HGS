package org.example.hamrogharsewa.repository;

import org.example.hamrogharsewa.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<ServiceCategory, String> {

    List<ServiceCategory> findByActiveTrue();

    Optional<ServiceCategory> findByNameIgnoreCase(String name);
}
