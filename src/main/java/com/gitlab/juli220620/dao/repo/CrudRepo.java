package com.gitlab.juli220620.dao.repo;

import java.util.List;

public interface CrudRepo <E, I> {

    E save(E entity);

    E findById(I id);

    List<E> getAll();

    E update(E entity);

    boolean delete(E entity);
}
