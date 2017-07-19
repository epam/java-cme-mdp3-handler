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

package com.epam.cme.mdp3.test.mbpbook;

import com.epam.cme.mdp3.FieldSet;

public class OrderBookPriceEntry extends ImpliedBookPriceEntry implements OrderBookPriceLevel {
    private int orderCount;

    @Override
    public int getOrderCount() {
        return orderCount;
    }

    @Override
    public void clear() {
        super.clear();
        orderCount = 0;
    }

    public void refreshFromAnotherEntry(final OrderBookPriceEntry bookEntry) {
        super.refreshFromAnotherEntry(bookEntry);
        this.orderCount = bookEntry.getOrderCount();
    }

    @Override
    public void refreshBookFromMessage(final FieldSet fieldSet) {
        super.refreshFromMessage(fieldSet);
        this.orderCount = fieldSet.getInt32(346);
    }
}
