package com.mdd.pocmdd.mapper;

import java.util.List;

/**
 * Interface générique pour mapper les entités et leurs DTOs.
 * Cette interface permet de définir les méthodes de conversion entre les entités et leurs DTOs.
 * 
 * @param <D> Le type du DTO.
 * @param <E> Le type de l'entité.
 */
public interface EntityMapper<D, E> {

    /**
     * Convertit un DTO en entité.
     * 
     * @param dto Le DTO à convertir.
     * @return L'entité correspondante.
     */
    E toEntity(D dto);

    /**
     * Convertit une entité en DTO.
     * 
     * @param entity L'entité à convertir.
     * @return Le DTO correspondant.
     */
    D toDto(E entity);

    /**
     * Convertit une liste de DTOs en une liste d'entités.
     * 
     * @param dtoList La liste de DTOs à convertir.
     * @return La liste d'entités correspondante.
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Convertit une liste d'entités en une liste de DTOs.
     * 
     * @param entityList La liste d'entités à convertir.
     * @return La liste de DTOs correspondante.
     */
    List<D> toDto(List<E> entityList);

}
