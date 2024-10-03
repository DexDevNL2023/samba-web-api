package com.teleo.manager.document.repositories.impl;

import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentRepositoryImpl extends GenericRepositoryImpl<DocumentRequest, Document> {

    public DocumentRepositoryImpl(EntityManager entityManager) {
        super(Document.class, entityManager);
    }
}
