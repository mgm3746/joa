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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class TestJdkUtil {
    @Test
    void testByteOptionValue() {
        assertEquals("256k", JdkUtil.getByteOptionValue("-Xss256k"), "Option value not correct.");
        assertEquals("2G", JdkUtil.getByteOptionValue("-Xmx2G"), "Option value not correct.");
        assertEquals("128M", JdkUtil.getByteOptionValue("-XX:MaxPermSize=128M"), "Option value not correct.");
        assertEquals("3865051136", JdkUtil.getByteOptionValue("-XX:InitialHeapSize=3865051136"),
                "Option value not correct.");
        assertEquals("7730102272", JdkUtil.getByteOptionValue("-XX:MaxHeapSize=7730102272"),
                "Option value not correct.");
        assertEquals("268435456", JdkUtil.getByteOptionValue("-XX:MaxPermSize=268435456"), "Option value not correct.");
        assertEquals("67108864", JdkUtil.getByteOptionValue("-XX:PermSize=67108864"), "Option value not correct.");
        assertNull(JdkUtil.getByteOptionValue(null), "Option value not correct.");
    }
}
