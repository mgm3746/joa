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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestJdkRegEx {
    @Test
    void testFileLinux() {
        String s = "/usr/lib64/libaio.so.1.0.1";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File not identified.");
    }

    @Test
    void testFileLinuxNoExtension() {
        String s = "/usr/lib64/mylibrary";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File not identified.");
    }

    @Test
    void testFileLinuxNoPath() {
        String s = "mylibrary";
        assertTrue(s.matches(JdkRegEx.FILE_PATH), "File not identified.");
    }

    @Test
    void testFileWindows() {
        String file = "E:\\path\\java\\bin\\server\\jvm.dll";
        assertTrue(file.matches(JdkRegEx.FILE_PATH), "File not recognized.");
    }

    @Test
    void testFileWindowsNoPath() {
        String file = "mylibrary.dll";
        assertTrue(file.matches(JdkRegEx.FILE_PATH), "File not recognized.");
    }
}
