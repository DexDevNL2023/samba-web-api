package com.teleo.manager.document.repositories;

import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends GenericRepository<DocumentRequest, Document> {

    @Query("SELECT DISTINCT d FROM Document d JOIN FETCH d.sinistre s WHERE s.id = :sinistreId")
    List<Document> findAllBySinistreId(@Param("sinistreId") Long sinistreId);

    @Query("SELECT DISTINCT d FROM Document d JOIN FETCH d.prestation p WHERE p.id = :prestationId")
    List<Document> findAllByPrestationId(@Param("prestationId") Long prestationId);
}
