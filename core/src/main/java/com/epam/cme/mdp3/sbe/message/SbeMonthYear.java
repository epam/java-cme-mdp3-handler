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

package com.epam.cme.mdp3.sbe.message;

import com.epam.cme.mdp3.sbe.message.meta.SbePrimitiveType;

public class SbeMonthYear {
    private int year;
    private short month;
    private short day;
    private short week;

    public static SbeMonthYear instance() {
        return new SbeMonthYear();
    }

    public boolean isNullYear() {
        return this.getYear() == SbePrimitiveType.UInt16.getNullValue();
    }

    public boolean isMonthYear() {
        return this.getMonth() == SbePrimitiveType.UInt8.getNullValue();
    }

    public boolean isDayYear() {
        return this.getDay() == SbePrimitiveType.UInt8.getNullValue();
    }

    public boolean isWeekYear() {
        return this.getWeek() == SbePrimitiveType.UInt8.getNullValue();
    }

    public short getDay() {
        return day;
    }

    public void setDay(short day) {
        this.day = day;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public short getWeek() {
        return week;
    }

    public void setWeek(short week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void reset() {
        setDay((short) SbePrimitiveType.UInt8.getNullValue());
        setWeek((short) SbePrimitiveType.UInt8.getNullValue());
        setMonth((short) SbePrimitiveType.UInt8.getNullValue());
        setYear((int) SbePrimitiveType.UInt16.getNullValue());
    }
}
