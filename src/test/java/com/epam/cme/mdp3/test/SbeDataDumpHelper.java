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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class SbeDataDumpHelper {
    public static final byte INIT_OFFSET = 10;
    public static final byte HEADER_OFFSET = 10;

    public static int getUInt16BigEndian(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xff) << 8) + (buffer[offset + 1] & 0xff);
    }

    public static byte[] loadTestPackets(final Path path) throws Exception {
        final File file = path.toFile();
        final int size = (int) file.length();
        final byte[] data = new byte[size];
        final FileInputStream in = new FileInputStream(file);
        in.read(data);
        in.close();
        return data;
    }

    public static class DataFileFinder extends SimpleFileVisitor<Path> {
        private Path dataFilePath;
        private String dataFilePrefix;

        DataFileFinder(final String dataFilePrefix) {
            this.dataFilePrefix = dataFilePrefix;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            if (file.getName(file.getNameCount() - 1).toString().startsWith(dataFilePrefix)) {
                dataFilePath = file;
                return FileVisitResult.TERMINATE;
            } else {
                return FileVisitResult.CONTINUE;
            }
        }
    }

    public static Path lookupDataFile(final int channelId, final String feedComp) throws IOException {
        final String dataFilePrefix = buildFilePrefix(channelId, feedComp);
        final DataFileFinder dataFileFinder = new DataFileFinder(dataFilePrefix);
        Files.walkFileTree(new File(".").toPath(), dataFileFinder);
        return dataFileFinder.dataFilePath;
    }

    public static String buildFilePrefix(final int channelId, final String feedComp) {
        return String.format("%1$d_%2$s_", channelId, feedComp);
    }
}
