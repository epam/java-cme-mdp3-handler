package com.epam.cme.mdp3.core.control;


public interface CircularBuffer<T> {

    /**
     * It copies the data from the entry.
     * @param entity
     */
    void add(T entity);

    /**
     * It returns the entities in sorted order and removes them after.
     * @return T or null if buffer is empty.
     */
    T remove();

    boolean isEmpty();
}
