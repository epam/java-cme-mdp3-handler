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

package com.epam.cme.mdp3.core.control;

public class InMemoryEventController implements EventController {
    private final static int INITIAL_LOG_SIZE = 64;
    private final static int INCREMENT_LOG_SIZE = 32;
    private int[] logContainer = new int[INITIAL_LOG_SIZE];

    @Override
    public void logSecurity(final int securityId) {
        for (int i = 0; i < logContainer.length; i++) {
            final int securityInLog = logContainer[i];
            if (securityInLog > 0) {
                if (securityInLog == securityId) {
                    return;
                }
            } else {
                logContainer[i] = securityId;
                return;
            }
        }
        final int idx = logContainer.length;
        resizeLogContainer();
        this.logContainer[idx] = securityId;
    }

    @Override
    public void commit(EventCommitFunction eventCommitFunction) {
        for (int i = 0; i < logContainer.length; i++) {
            if (logContainer[i] > 0) {
                eventCommitFunction.onCommit(logContainer[i]);
            } else break;
        }
        reset();
    }

    @Override
    public void reset() {
        for (int i = 0; i < logContainer.length; i++) {
            logContainer[i] = 0;
        }
    }

    private void resizeLogContainer() {
        final int[] newLogContainer = new int[logContainer.length + INCREMENT_LOG_SIZE];
        for (int i = 0; i < logContainer.length; i++) {
            newLogContainer[i] = logContainer[i];
        }
        logContainer = newLogContainer;
    }
}
