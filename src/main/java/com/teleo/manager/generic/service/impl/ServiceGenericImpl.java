package com.teleo.manager.generic.service.impl;

import jakarta.transaction.Transactional;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.generic.dto.reponse.PagedResponse;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.mapper.GenericMapper;
import com.teleo.manager.generic.repository.GenericRepository;
import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.generic.utils.GenericUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class ServiceGenericImpl<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>> implements ServiceGeneric<D, R, E> {

    protected final Class<E> entityClass;
    protected final GenericRepository<D, E> repository;
    protected final GenericMapper<D, R, E> mapper;

    public ServiceGenericImpl(Class<E> entityClass, GenericRepository<D, E> repository, GenericMapper<D, R, E> mapper) {
        this.entityClass = entityClass;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @LogExecution
    public R save(D dto) throws RessourceNotFoundException {
        E e = mapper.toEntity(dto);
        e = repository.save(e);
        return getOne(e);
    }

    @Override
    @Transactional
    @LogExecution
    public E saveDefault(E entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> saveAll(List<D> dtos) {
        dtos.forEach(this::save);
        return getAll();
    }

    @Override
    @Transactional
    @LogExecution
    public R update(D dto, Long id) {
        E entity = getById(id);
        if (entity.equalsToDto(dto))
            throw new RessourceNotFoundException("La ressource " + this.entityClass.getSimpleName() + " avec les données suivante : " + dto.toString() + " existe déjà");
        entity.update(mapper.toEntity(dto));
        entity = repository.save(entity);
        return getOne(entity);
    }

    @Override
    @Transactional
    @LogExecution
    public Boolean exist(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> getAll() {
        return repository.findAll().stream().filter(e -> !e.getIsDeleted()).map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogExecution
    public List<R> getAllByIds(List<Long> ids) {
        return repository.findAllById(ids).stream().filter(e -> !e.getIsDeleted()).map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogExecution
    public List<Long> getAllToIds(List<E> entities) {
        return entities.stream()
                .map(this::getOneToId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @LogExecution
    public PagedResponse<R> getAllByPage(Integer page, Integer size) {
        // Vérifier la syntaxe de page et size
        GenericUtils.validatePageNumberAndSize(page, size);
        // Construire la pagination
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.PERIODE_FILTABLE_FIELD);
        // on récupere les données
        Page<E> list = repository.findAllByIsDeleted(false, pageable);
        if (list.getNumberOfElements() == 0)
            throw new RessourceNotFoundException("La recherche de " + this.entityClass.getSimpleName() + " est vide!");
        // Mapper Dto
        List<R> listDto = mapper.toDto(list.getContent());
        return new PagedResponse<R>(listDto, list.getNumber(), list.getSize(), list.getTotalElements(), list.getTotalPages(), list.isLast());
    }

    @Override
    @Transactional
    @LogExecution
    public E getById(Long id) {
        return repository.findById(id).filter(e -> !e.getIsDeleted()).orElse(null);
    }

    @Override
    @Transactional
    @LogExecution
    public R getOne(E entity) {
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    @LogExecution
    public Long getOneToId(E entity) {
        return entity != null ? entity.getId() : null;
    }

    @Override
    @Transactional
    @LogExecution
    public void delete(Boolean isAdmin, Long id) {
        if (!exist(id))
            throw new RessourceNotFoundException("La ressource " + this.entityClass.getSimpleName() + " avec l'id " + id + " n'existe pas");
        E entity = getById(id);
        if (isAdmin) {
            repository.deleteById(entity.getId());
        } else {
            entity.setIsDeleted(true);
            repository.save(entity);
        }
    }

    @Override
    @Transactional
    @LogExecution
    public void deleteAll(Boolean isAdmin, List<Long> ids) {
        ids.forEach(id -> delete(isAdmin, id));
    }

    @Override
    @Transactional
    @LogExecution
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (value == null) {
            return false;
        }
        return this.repository.existsByFieldValue(value, fieldName);
    }
}
