/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2023 Mike Millson                                                                                    *
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

import org.junit.jupiter.api.Test;

public class TestJdkRegEx {
    @Test
    void testFile() {
        String s = "libaio.so.1.0.1";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileDash() {
        String s = "my-library";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileDollarSign() {
        String s = "my$library";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileEndingWhitespace() {
        String s = "mylibrary ";
        assertFalse(s.matches(JdkRegEx.DIR_FILE), "File incorrectly identified.");
        assertFalse(s.matches(JdkRegEx.FILE_PATH), "File path incorrectly identified.");
    }

    @Test
    void testFileNoDirectoryNoExtension() {
        String s = "mylibrary";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFilePlus() {
        String s = "libstdc++.so.6.0.19";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileSpace() {
        String s = "my library.dll";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileSpaceNoExtension() {
        String s = "my library";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testFileUnderscore() {
        String s = "my_library";
        assertTrue(s.matches(JdkRegEx.DIR_FILE), "File not identified.");
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals(s, JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathDirectoryAmpersand() {
        String s = "/tmp/hsperfdata_first.last@location/12345";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals("12345", JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathDirectoryLinux() {
        String s = "/usr/lib64/";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertNull(JdkRegEx.getFile(s), "File incorrectly indentified.");
    }

    @Test
    void testPathDirectoryWindows() {
        String s = "E:\\path\\java\\bin\\server\\";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not recognized.");
        assertNull(JdkRegEx.getFile(s), "File incorrectly indentified.");
    }

    @Test
    void testPathFileLinux() {
        String s = "/usr/lib64/libaio.so.1.0.1";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals("libaio.so.1.0.1", JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathFileLinuxEndingWhitespace() {
        String s = "/usr/lib64/mylibrary ";
        assertFalse(s.matches(JdkRegEx.FILE_PATH), "File path incorrectly identified.");
        assertEquals("mylibrary", JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathFileLinuxNoExtension() {
        String s = "/usr/lib64/mylibrary";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not identified.");
        assertEquals("mylibrary", JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathFileWindows() {
        String s = "E:\\path\\java\\bin\\server\\jvm.dll";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not recognized.");
        assertEquals("jvm.dll", JdkRegEx.getFile(s), "File not identified.");
    }

    @Test
    void testPathFileWindowsShortFileName() {
        String s = "C:\\Users\\MYUSER~1\\AppData\\Local\\Temp\\jna-1234\\jna5678.dll";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File path not recognized.");
        assertEquals("jna5678.dll", JdkRegEx.getFile(s), "File not identified.");
    }
}
