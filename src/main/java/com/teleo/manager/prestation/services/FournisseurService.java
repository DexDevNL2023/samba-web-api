package com.teleo.manager.prestation.services;

import com.teleo.manager.generic.service.ServiceGeneric;
import com.teleo.manager.prestation.dto.reponse.FournisseurResponse;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import com.teleo.manager.prestation.entities.Fournisseur;

import java.util.List;

public interface FournisseurService extends ServiceGeneric<FournisseurRequest, FournisseurResponse, Fournisseur> {
    Fournisseur saveDefault(Fournisseur fournisseur, List<Long> branchIds);
    List<FournisseurResponse> findFournisseurWithBranchesById(Long branchId);
    FournisseurResponse findFournisseurWithPrestationsById(Long prestationId);
    FournisseurResponse findFournisseurWithRegistrantsById(Long branchId);
}
