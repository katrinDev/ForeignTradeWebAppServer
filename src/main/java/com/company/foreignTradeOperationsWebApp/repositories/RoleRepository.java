package com.company.foreignTradeOperationsWebApp.repositories;

import com.company.foreignTradeOperationsWebApp.models.RoleEntity;
import com.company.foreignTradeOperationsWebApp.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByRoleName(RoleEnum roleName);
}
