package com.teleo.manager.document.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.document.enums.TypeDocument;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentResponse extends BaseResponse {
    private String numeroDocument;
    private String nom;
    private TypeDocument type;
    private String description;
    private String url;
    private Long sinistre;
    private Long prestation;
}
