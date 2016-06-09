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

package com.epam.cme.mdp3.mktdata;

import com.epam.cme.mdp3.FieldSet;
import com.epam.cme.mdp3.core.channel.ChannelContext;
import com.epam.cme.mdp3.mktdata.enums.MDUpdateAction;

abstract class AbstractOrderBookHandler<T> extends AbstractMktDataHandler {
    boolean refreshedTop = false;
    boolean refreshedBook = false;

    AbstractOrderBookHandler(final ChannelContext channelContext, final int securityId, final int subscriptionFlags) {
        super(channelContext, securityId, subscriptionFlags);
    }

    @Override
    abstract public void clear();

    public boolean isRefreshedTop() {
        return refreshedTop;
    }

    public void logRefreshedTop() {
        this.refreshedTop = true;
    }

    public void resetTransaction() {
        this.refreshedTop = false;
    }

    protected abstract void modifyEntry(final T[] levelEntries, final int level, final FieldSet fieldSet);

    protected abstract void deleteEntry(final T[] levelEntries, final int level);

    protected abstract void deleteFrom(final T[] levelEntries, final int n);

    protected abstract void deleteThru(final T[] levelEntries);

    protected abstract void insertEntry(final T[] levelEntries, final int level, final FieldSet fieldSet);


    protected void handleIncrementRefresh(final T[] levelEntries, final int level,
                                          final MDUpdateAction updateAction, final FieldSet incrementEntry) {
        switch (updateAction) {
            case Overlay:
            case Change:
                modifyEntry(levelEntries, level, incrementEntry);
                break;
            case New:
                insertEntry(levelEntries, level, incrementEntry);
                break;
            case Delete:
                deleteEntry(levelEntries, level);
                break;
            case DeleteFrom:
                deleteFrom(levelEntries, level);
                break;
            case DeleteThru:
                deleteThru(levelEntries);
                break;
            default:
                throw new IllegalStateException();
        }
    }

}
