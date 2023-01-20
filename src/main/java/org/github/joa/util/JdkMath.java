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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Math utility methods and constants for OpenJDK.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkMath {

    /**
     * Calculate percent.
     * 
     * @param part
     *            The numerator.
     * @param whole
     *            The denominator.
     * 
     * @return Percent part:whole rounded to the nearest whole number.
     */
    public static int calcPercent(final long part, final long whole) {
        if (part < 0) {
            throw new IllegalArgumentException("part: " + part);
        }
        if (whole < 0) {
            throw new IllegalArgumentException("whole: " + whole);
        }
        int percent;
        if (whole == 0) {
            if (part == 0 && whole == 0) {
                percent = 100;
            } else {
                percent = Integer.MAX_VALUE;
            }
        } else {
            BigDecimal calc = new BigDecimal(part);
            BigDecimal hundred = new BigDecimal("100");
            calc = calc.multiply(hundred);
            calc = calc.divide(new BigDecimal(whole), 0, RoundingMode.HALF_EVEN);
            percent = calc.intValue();
        }
        return percent;
    }

    /**
     * Make default constructor private so the class cannot be instantiated.
     */
    private JdkMath() {

    }
}
