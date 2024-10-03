package com.teleo.manager.document.mapper;

import com.teleo.manager.document.dto.reponse.DocumentResponse;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.prestation.services.PrestationService;
import com.teleo.manager.sinistre.services.SinistreService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SinistreService.class, PrestationService.class})
public interface DocumentMapper extends GenericMapper<DocumentRequest, DocumentResponse, Document> {
}
