package com.teleo.manager.generic.repository;

import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<D extends BaseRequest, E extends BaseEntity<E, D>> extends JpaRepository<E, Long> {
    boolean existsByFieldValue(Object value, String fieldName);
    Page<E> findAllByIsDeleted(boolean isDeleted, Pageable pageable);
}
