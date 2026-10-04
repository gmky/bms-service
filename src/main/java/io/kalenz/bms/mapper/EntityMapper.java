package io.kalenz.bms.mapper;

import java.util.List;

public interface EntityMapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
    List<E> toEntity(List<D> dtos);
    List<D> toDto(List<E> entities);
}
