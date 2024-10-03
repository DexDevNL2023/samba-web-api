package com.teleo.manager.authentification.repositories.impl;

import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.repository.impl.GenericRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl extends GenericRepositoryImpl<AccountRequest, Account> {
    public AccountRepositoryImpl(EntityManager entityManager) {
        super(Account.class, entityManager);
    }
}
