package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Boolean existsByWorkEmail(String email);

    Boolean existsByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);

    PersonEntity findByWorkEmail(String email);
}
