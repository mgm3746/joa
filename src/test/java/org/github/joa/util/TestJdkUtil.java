/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2025 Mike Millson                                                                                    *
 *                                                                                                                    *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License       *
 * v. 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0 which is    *
 * available at https://www.apache.org/licenses/LICENSE-2.0.                                                          *
 *                                                                                                                    *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0                                                                     *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.github.joa.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.github.joa.domain.GarbageCollector;
import org.junit.jupiter.api.Test;

public class TestJdkUtil {

    @Test
    void testByteOptionValue() {
        assertEquals(512L * 1024, JdkUtil.getByteOptionBytes("512K"), "-Xss not correct.");
        assertEquals("256k", JdkUtil.getByteOptionValue("-Xss256k"), "Option value not correct.");
        assertEquals("2G", JdkUtil.getByteOptionValue("-Xmx2G"), "Option value not correct.");
        assertEquals("128M", JdkUtil.getByteOptionValue("-XX:MaxPermSize=128M"), "Option value not correct.");
        assertEquals(2048L * 1024 * 1024, JdkUtil.getByteOptionBytes("2048m"), "-XX:MaxMetaspaceSize not correct.");
        assertEquals("3865051136", JdkUtil.getByteOptionValue("-XX:InitialHeapSize=3865051136"),
                "Option value not correct.");
        assertEquals("7730102272", JdkUtil.getByteOptionValue("-XX:MaxHeapSize=7730102272"),
                "Option value not correct.");
        assertEquals("268435456", JdkUtil.getByteOptionValue("-XX:MaxPermSize=268435456"), "Option value not correct.");
        assertEquals("67108864", JdkUtil.getByteOptionValue("-XX:PermSize=67108864"), "Option value not correct.");
        assertNull(JdkUtil.getByteOptionValue(null), "Option value not correct.");
    }

    @Test
    void testDefaultGarbageCollectorsJdk21() {
        List<GarbageCollector> garbageCollectors = JdkUtil.getDefaultGarbageCollectors(21);
        assertFalse(garbageCollectors.contains(GarbageCollector.UNKNOWN), "Collector unknown.");
        assertEquals(1, garbageCollectors.size(), "Number of default collectors not correct.");
        assertTrue(garbageCollectors.contains(GarbageCollector.G1), "Default collector not correct.");
    }

    @Test
    void testDefaultGarbageCollectorsJdk25() {
        List<GarbageCollector> garbageCollectors = JdkUtil.getDefaultGarbageCollectors(25);
        assertFalse(garbageCollectors.contains(GarbageCollector.UNKNOWN), "Collector unknown.");
        assertEquals(1, garbageCollectors.size(), "Number of default collectors not correct.");
        assertTrue(garbageCollectors.contains(GarbageCollector.G1), "Default collector not correct.");
    }

    void testFilePathOptionValue() {
        assertEquals("/path/to/heap.hprof", JdkUtil.getFilePathOptionValue("-XX:HeapDumpPath=/path/to/heap.hprof"),
                "Option value not correct.");
        assertNull(JdkUtil.getFilePathOptionValue(null), "Option value not correct.");
    }

    @Test
    void testIntegerOptionValue() {
        assertEquals(9, JdkUtil.getIntegerOptionValue("-XX:MaxTenuringThreshold=9"), "Option value not correct.");
        assertEquals(Constants.UNKNOWN, JdkUtil.getIntegerOptionValue(null), "Option value not correct.");
    }

    @Test
    void testPercentOptionValue() {
        assertEquals("60", JdkUtil.getPercentOptionValue("-XX:MaxRAMPercentage=60"), "Option value not correct.");
        assertEquals("60.0", JdkUtil.getPercentOptionValue("-XX:MaxRAMPercentage=60.000000"),
                "Option value not correct.");
        assertEquals("60.0", JdkUtil.getPercentOptionValue("-XX:MaxRAMPercentage=60.0"), "Option value not correct.");
        assertEquals("60.004", JdkUtil.getPercentOptionValue("-XX:MaxRAMPercentage=60.004"),
                "Option value not correct.");
        assertEquals("60.004", JdkUtil.getPercentOptionValue("-XX:MaxRAMPercentage=60.0040000"),
                "Option value not correct.");
        assertNull(JdkUtil.getPercentOptionValue(null), "Option value not correct.");
    }

    void testStringOptionValue() {
        assertEquals("generational", JdkUtil.getStringOptionValue("-XX:ShenandoahGCMode=generational"),
                "Option value not correct.");
        assertNull(JdkUtil.getStringOptionValue(null), "Option value not correct.");
    }
}
