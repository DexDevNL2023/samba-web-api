package com.teleo.manager.generic.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.lang.reflect.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Transactional
public abstract class GenericRepositoryImpl<D extends BaseRequest, E extends BaseEntity<E, D>> extends SimpleJpaRepository<E, Long> implements GenericRepository<D, E> {
    private final Class<E> clazz;
    private final EntityManager entityManager;

    public GenericRepositoryImpl(Class<E> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
        this.clazz = clazz;
        this.entityManager = entityManager;
    }

    @Override
    public boolean existsByFieldValue(Object value, String fieldName) {
        try {
            if (!isFieldExist(fieldName)) {
                throw new UnsupportedOperationException("Le champ " + fieldName + " n'est pas pris en charge");
            }
            String queryString = String.format("SELECT x FROM %s x WHERE x.%s = :value",
                    this.clazz.getSimpleName(),
                    fieldName);
            TypedQuery<E> query = entityManager.createQuery(queryString, clazz);
            query.setParameter("value", value);
            return query.getResultList().size() > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Page<E> findAllByIsDeleted(boolean isDeleted, Pageable pageable) {
        String queryString = String.format("SELECT x FROM %s x WHERE x.isDeleted = :isDeleted",
                clazz.getSimpleName());

        TypedQuery<E> query = entityManager.createQuery(queryString, clazz);
        query.setParameter("isDeleted", isDeleted);

        // Get the total count for pagination
        long total = entityManager.createQuery(
                        String.format("SELECT COUNT(x) FROM %s x WHERE x.isDeleted = :isDeleted", clazz.getSimpleName()), Long.class)
                .setParameter("isDeleted", isDeleted)
                .getSingleResult();

        // Fetch the results based on the pageable object
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    public boolean isFieldExist(String fieldName) {
        for (Field field : clazz.getFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
}