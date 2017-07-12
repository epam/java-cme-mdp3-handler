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


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractMktDataHandler {
    protected int securityId;
    protected int subscriptionFlags;
    private Lock lock = new ReentrantLock();

    public AbstractMktDataHandler(final int securityId, final int subscriptionFlags) {
        this.securityId = securityId;
        this.subscriptionFlags = subscriptionFlags;
    }

    public abstract void clear();

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    public int getSecurityId() {
        return securityId;
    }

    public int getSubscriptionFlags() {
        return subscriptionFlags;
    }

    public void setSubscriptionFlags(int subscriptionFlags) {
        this.subscriptionFlags = subscriptionFlags;
    }
}
