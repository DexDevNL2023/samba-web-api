package com.teleo.manager.authentification.mapper;

import com.teleo.manager.authentification.dto.request.SignupRequest;
import org.mapstruct.*;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.services.RoleService;
import com.teleo.manager.generic.mapper.GenericMapper;

@Mapper(componentModel = "spring", uses = {RoleService.class})
public interface AccountMapper extends GenericMapper<AccountRequest, AccountResponse, Account> {
    Account toEntity(SignupRequest dto);
}
