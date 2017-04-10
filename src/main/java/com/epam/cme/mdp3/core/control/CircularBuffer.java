package com.epam.cme.mdp3.core.control;


public interface CircularBuffer<T> {
    void add(T entity);

    /**
     * It returns and removes the entities in sorted order.
     * @return T
     */
    T remove();
}
