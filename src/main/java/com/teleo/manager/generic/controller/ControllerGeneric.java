package com.teleo.manager.generic.controller;

import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.generic.dto.reponse.PagedResponse;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.utils.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ControllerGeneric<D extends BaseRequest, R extends BaseResponse, E extends BaseEntity<E, D>> {
    ResponseEntity<RessourceResponse<R>> save(@Valid @RequestBody D dto);

    ResponseEntity<RessourceResponse<List<R>>> saveAll(@NotEmpty @Valid @RequestBody List<D> dtos);

    ResponseEntity<RessourceResponse<R>> deleteById(@NotNull @PathVariable("id") Long id);

    ResponseEntity<RessourceResponse<R>> deleteAll(@NotEmpty @RequestParam("ids") List<Long> ids);

    ResponseEntity<RessourceResponse<R>> getOne(@NotNull @PathVariable("id") Long id);

    ResponseEntity<RessourceResponse<List<R>>> getAll();

    ResponseEntity<RessourceResponse<List<R>>> getAllByIds(@NotEmpty @RequestParam("ids") List<Long> ids);

    ResponseEntity<RessourceResponse<PagedResponse<R>>> getByPage(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    );

    ResponseEntity<RessourceResponse<R>> update(@Valid @RequestBody D dto, @PathVariable("id") Long id);
}

