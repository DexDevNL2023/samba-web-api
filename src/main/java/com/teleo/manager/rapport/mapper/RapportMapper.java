package com.teleo.manager.rapport.mapper;

import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.rapport.dto.reponse.RapportResponse;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.entities.Rapport;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RapportMapper extends GenericMapper<RapportRequest, RapportResponse, Rapport> {
}
