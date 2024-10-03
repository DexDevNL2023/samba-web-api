package com.teleo.manager.authentification.controllers.impl;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.teleo.manager.authentification.controllers.PermissionController;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.authentification.services.PermissionService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/permissions")
@Tag(name = "Permissions", description = "API de gestion des permissions")
public class PermissionControllerImpl extends ControllerGenericImpl<PermissionRequest, PermissionResponse, Permission> implements PermissionController {
    protected PermissionControllerImpl(PermissionService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
    }

    @Override
    protected Permission newInstance() {
        return new Permission();
    }
}
