package com.teleo.manager.parametre.repositories;

import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends GenericRepository<CompanyRequest, Company> {

    @Query("SELECT DISTINCT c FROM Company c ORDER BY c.id ASC")
    Company findFirstByOrderByIdAsc();
}
