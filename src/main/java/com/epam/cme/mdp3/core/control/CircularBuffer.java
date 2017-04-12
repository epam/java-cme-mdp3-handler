package com.epam.cme.mdp3.core.control;


public interface CircularBuffer<T> {
    void add(T entity);

    /**
     * It returns the entities in sorted order and removes them after.
     * @return T or null if buffer is empty.
     */
    T remove();

    boolean isEmpty();
}
