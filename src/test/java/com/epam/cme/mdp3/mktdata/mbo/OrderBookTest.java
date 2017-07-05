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

package com.epam.cme.mdp3.mktdata.mbo;

import com.epam.cme.mdp3.mktdata.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookTest {
    private OrderBook orderBook;

    @BeforeEach
    public void init(){
        orderBook = null;
    }

    @Test
    public void gettingOrderForParticularLevelToShowThem(){
        Price particularBidLevel = new Price();
        TreeSet<OrderEntity> bids = orderBook.getBids(particularBidLevel);
        for (OrderEntity bid : bids) {
            System.out.println(bid);
        }

        Price particularAskLevel = new Price();
        TreeSet<OrderEntity> asks = orderBook.getOffers(particularAskLevel);
        for (OrderEntity ask : asks) {
            System.out.println(ask);
        }
    }

    @Test
    public void understandingCurrentPositionByOrderId(){
        long brokersOrderId = 12345;
        int currentPosition = orderBook.getBidPosition(brokersOrderId);
        OrderEntity orderEntity = orderBook.getBid(brokersOrderId);
        TreeSet<OrderEntity> bids = orderBook.getBids(orderEntity.getPrice());
        System.out.println("Current position of order id '" + brokersOrderId + "' is " + currentPosition + " of " + bids.size());
    }
}