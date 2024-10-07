package dev.ddanny165.taskManagement.rest.mappers;

public interface DTOMapper<E, D> {
    D mapTo(E entity);
    E mapFrom(D dto);
}
