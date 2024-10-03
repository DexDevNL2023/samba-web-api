package com.teleo.manager.document.services;

import com.teleo.manager.document.dto.reponse.DocumentResponse;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.generic.service.ServiceGeneric;

import java.io.IOException;
import java.util.List;

public interface DocumentService extends ServiceGeneric<DocumentRequest, DocumentResponse, Document> {
    List<DocumentResponse> findAllBySinistreId(Long sinistreId);
    List<DocumentResponse> findAllByPrestationId(Long prestationId);
    byte[] downloadFile(String fileName) throws IOException;
}
