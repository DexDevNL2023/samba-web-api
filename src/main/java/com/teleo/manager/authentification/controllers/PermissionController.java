package com.teleo.manager.authentification.controllers;

import com.teleo.manager.authentification.dto.reponse.PermissionResponse;
import com.teleo.manager.authentification.dto.request.PermissionRequest;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.generic.controller.ControllerGeneric;

public interface PermissionController extends ControllerGeneric<PermissionRequest, PermissionResponse, Permission> {
}
