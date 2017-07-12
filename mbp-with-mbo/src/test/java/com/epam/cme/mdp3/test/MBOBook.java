/*
 * Copyright 2004-2016 EPAM Systems
 * This file is part of Java Market Data Handler for CME Market Data (MDP 3.0).
 * Java Market Data Handler for CME Market Data (MDP 3.0) is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Java Market Data Handler for CME Market Data (MDP 3.0) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Java Market Data Handler for CME Market Data (MDP 3.0).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.epam.cme.mdp3.test;

import java.util.*;

public class MBOBook {
    private Set<BookEntity> bidEntities = Collections.synchronizedSet(new TreeSet<>(new BidComparator()));
    private Set<BookEntity> askEntities = Collections.synchronizedSet(new TreeSet<>(new AskComparator()));


    public boolean remove(Side side, BookEntity entityToRemove){
        Iterator<BookEntity> iterator;
        boolean result = false;
        if(side.equals(Side.BID)) {
            iterator = bidEntities.iterator();
        } else {
            iterator = askEntities.iterator();
        }
        while (iterator.hasNext()){
            BookEntity bookEntity = iterator.next();
            if(entityToRemove.getOrderId() == bookEntity.getOrderId()){
                iterator.remove();
                result = true;
                break;
            }
        }
        return result;
    }

    public void add(Side side, BookEntity entityToAdd){
        if(side.equals(Side.BID)) {
            bidEntities.add(entityToAdd);
        } else {
            askEntities.add(entityToAdd);
        }
    }

    public boolean update(Side side, BookEntity entityToUpdate){
        Set<BookEntity> entities;
        boolean result = false;
        if(side.equals(Side.BID)) {
            entities = bidEntities;
        } else {
            entities = askEntities;
        }
        Iterator<BookEntity> iterator = entities.iterator();
        while (iterator.hasNext()){
            BookEntity bookEntity = iterator.next();
            if(entityToUpdate.getOrderId() == bookEntity.getOrderId()){
                iterator.remove();
                entities.add(entityToUpdate);
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("___________________BIDS___________________\n");
        bidEntities.forEach(bookEntity ->
            sb.append(bookEntity.getMdEntryPx()).append("             ").append(bookEntity.getMdDisplayQty()).append("\n")
        );
        sb.append("___________________ASKS___________________\n");
        askEntities.forEach(bookEntity ->
            sb.append(bookEntity.getMdEntryPx()).append("             ").append(bookEntity.getMdDisplayQty()).append("\n")
        );
        return sb.toString();
    }

    public void clear() {
        bidEntities.clear();
        askEntities.clear();
    }


    public enum Side {
        BID, ASK
    }

    class BidComparator implements Comparator<BookEntity> {
        @Override
        public int compare(BookEntity o1, BookEntity o2) {
            int priceCompared = Double.compare(o2.getMdEntryPx(), o1.getMdEntryPx());
            if(priceCompared == 0){
                return Long.compare(o1.getMdOrderPriority(), o2.getMdOrderPriority());
            } else {
                return priceCompared;
            }
        }
    }

    class AskComparator implements Comparator<BookEntity> {
        @Override
        public int compare(BookEntity o1, BookEntity o2) {
            int priceCompared = Double.compare(o1.getMdEntryPx(), o2.getMdEntryPx());
            if(priceCompared == 0){
                return Long.compare(o1.getMdOrderPriority(), o2.getMdOrderPriority());
            } else {
                return priceCompared;
            }
        }
    }


    public static class BookEntity {
        private long mdOrderPriority;
        private double mdEntryPx;
        private long mdDisplayQty;
        private long orderId;

        public BookEntity(long orderId, long mdOrderPriority, double mdEntryPx, long mdDisplayQty) {
            this.mdOrderPriority = mdOrderPriority;
            this.mdEntryPx = mdEntryPx;
            this.orderId = orderId;
            this.mdDisplayQty = mdDisplayQty;
        }

        public long getMdOrderPriority() {
            return mdOrderPriority;
        }

        public double getMdEntryPx() {
            return mdEntryPx;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BookEntity that = (BookEntity) o;

            if (mdOrderPriority != that.mdOrderPriority) return false;
            return Double.compare(that.mdEntryPx, mdEntryPx) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = (int) (mdOrderPriority ^ (mdOrderPriority >>> 32));
            temp = Double.doubleToLongBits(mdEntryPx);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        public long getOrderId() {
            return orderId;
        }

        public long getMdDisplayQty() {
            return mdDisplayQty;
        }
    }
}
