package com.teleo.manager.authentification.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.dto.reponse.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse extends BaseResponse {
    private String fullName;
    private String email;
    private String langKey;
    private String login;
    private Boolean usingQr;
    private String loginUrl;
    private String imageUrl;
    private Boolean actived;
    private Authority authority;
    private List<Long> roles = new ArrayList<>();
}
