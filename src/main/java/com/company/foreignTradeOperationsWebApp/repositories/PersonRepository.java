package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Boolean existsByWorkEmail(String email);

    PersonEntity findByWorkEmail(String email);
}
