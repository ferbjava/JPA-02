package com.capgemini.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, K extends Serializable> {

    T getOne(K id);

    T findOne(K id);

    List<T> findAll();

    T update(T entity);

    boolean exists(K id);
}
