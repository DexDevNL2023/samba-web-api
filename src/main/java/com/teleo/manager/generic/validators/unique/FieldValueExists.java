package com.teleo.manager.generic.validators.unique;

public interface FieldValueExists {
    /**
     * Vérifie si une valeur donnée existe ou non, pour un champ donné
     *
     * @param value     La valeur à vérifier
     * @param fieldName Le nom du champ pour lequel vérifier si la valeur existe
     * @return True si la valeur existe pour le champ, faux sinon
     * @throwsUnsupportedOperationException
     */
    boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}
