package com.teleo.manager.generic.entity;

public interface GenericEntity<E, D> {
    // met à jour l'instance actuelle avec les données fournies
    void update(E source);

    boolean equalsToDto(D source);

    String getEntityName();

    String getModuleName();
}
