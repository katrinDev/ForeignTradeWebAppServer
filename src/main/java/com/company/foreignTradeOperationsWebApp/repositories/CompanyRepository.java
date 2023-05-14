package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByCompanyName(String name);

    CompanyEntity findByCompanyName(String name);
}
