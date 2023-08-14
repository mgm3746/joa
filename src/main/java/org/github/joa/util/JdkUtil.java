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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.joa.domain.GarbageCollector;

/**
 * <p>
 * Utility methods and constants.
 * </p>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkUtil {

    /**
     * @param size
     *            The size in fromUnits.
     * @param fromUnits
     *            Current units.
     * @param toUnits
     *            Conversion units.
     * @return The size in toUnits.
     */
    public static long convertSize(final long size, char fromUnits, char toUnits) {
        if (fromUnits == toUnits) {
            return size;
        } else {
            if (!"bBkKmMgG".matches("^.*" + Character.toString(toUnits) + ".*$")) {
                throw new AssertionError("Unexpected toUnits value: " + toUnits);
            }
            BigDecimal newSize = new BigDecimal(size);
            switch (fromUnits) {
            case 'b':
            case 'B':
                if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.divide(Constants.KILOBYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.GIGABYTE);
                }
                break;
            case 'k':
            case 'K':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.divide(Constants.KILOBYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                }
                break;
            case 'm':
            case 'M':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.MEGABYTE);
                } else if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                } else if (toUnits == 'g' || toUnits == 'G') {
                    newSize = newSize.divide(Constants.MEGABYTE);
                }
                break;
            case 'g':
            case 'G':
                if (toUnits == 'b' || toUnits == 'B') {
                    newSize = newSize.multiply(Constants.GIGABYTE);
                } else if (toUnits == 'k' || toUnits == 'K') {
                    newSize = newSize.multiply(Constants.MEGABYTE);
                } else if (toUnits == 'm' || toUnits == 'M') {
                    newSize = newSize.multiply(Constants.KILOBYTE);
                }
                break;
            default:
                throw new AssertionError("Unexpected fromUnits value: " + fromUnits);
            }
            newSize = newSize.setScale(0, RoundingMode.HALF_EVEN);
            return newSize.longValue();
        }
    }

    /**
     * Get the bytes of a JVM option that specifies a byte value. For example, the bytes for <code>128k</code> is 128 x
     * 1024 = 131,072.
     * 
     * @param optionValue
     *            The JVM option value.
     * @return The JVM option value in bytes, or <code>Constants.UNKNOWN</code> if the option does not exist
     */
    public static final long getByteOptionBytes(final String optionValue) {
        long bytes = Constants.UNKNOWN;
        if (optionValue != null) {
            char fromUnits;
            long value;
            Pattern pattern = Pattern.compile(JdkRegEx.OPTION_SIZE_BYTES);
            Matcher matcher = pattern.matcher(optionValue);
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(2));
                if (matcher.group(3) != null) {
                    fromUnits = matcher.group(3).charAt(0);
                } else {
                    fromUnits = 'B';
                }
                char toUnits = 'B';
                if (fromUnits == toUnits) {
                    bytes = value;
                } else {
                    bytes = JdkUtil.convertSize(value, fromUnits, toUnits);
                }
            }
        }
        return bytes;
    }

    /**
     * Get the value of a JVM option that specifies a byte value. For example, the value for <code>-Xss128k</code> is
     * 128k. The value for <code>-XX:PermSize=128M</code> is 128M.
     * 
     * @param option
     *            The JVM option.
     * @return The JVM option value, or null if the option does not exist.
     */
    public static final String getByteOptionValue(final String option) {
        String value = null;
        if (option != null) {
            String regex = "^-[a-zA-Z:.]+={0,1}(" + JdkRegEx.OPTION_SIZE_BYTES + ")$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = matcher.group(1);
            }
        }
        return value;
    }

    /**
     * @param jdkVersionMajor
     *            The JDK major version.
     * @return The default garbage collector(s) for a given JDK version.
     */
    public static List<GarbageCollector> getDefaultGarbageCollectors(int jdkVersionMajor) {
        List<GarbageCollector> collectors = new ArrayList<GarbageCollector>();
        if (jdkVersionMajor >= 11 && jdkVersionMajor <= 17) {
            collectors.add(GarbageCollector.G1);
        } else if (jdkVersionMajor >= 8 && jdkVersionMajor <= 9) {
            collectors.add(GarbageCollector.PARALLEL_SCAVENGE);
            collectors.add(GarbageCollector.PARALLEL_OLD);
        } else {
            collectors.add(GarbageCollector.UNKNOWN);
        }
        return collectors;
    }

    /**
     * Get the value of a JVM option that specifies a file path value.
     * 
     * For example:
     * 
     * The value for <code>-XX:HeapDumpPath=/path/to/heap.hprof</code> is "/path/to/heap.hprof".
     * 
     * @param option
     *            The JVM option.
     * @return The JVM option value, or null if the option does not exist.
     */
    public static final String getFilePathOptionValue(final String option) {
        String value = null;
        if (option != null) {
            String regex = "^-[a-zA-Z:]+=(" + JdkRegEx.FILE_PATH + ")$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = matcher.group(1);
            }
        }
        return value;
    }

    /**
     * Get the value of a JVM option that specifies a number value.
     * 
     * For example:
     * <ul>
     * <li>The value for <code>-XX:MaxTenuringThreshold=9</code> is 9.</li>
     * <li>The value for <code>-Dsun.rmi.dgc.client.gcInterval=3600000</code> is 3600000.</li>
     * </ul>
     * 
     * @param option
     *            The JVM option or system property.
     * @return The JVM option or system property value, or <code>Integer.MIN_VALUE</code> if the option does not exist.
     */
    public static final long getIntegerOptionValue(final String option) {
        long value = Constants.UNKNOWN;
        if (option != null) {
            String regex = "^.+=(\\d{1,19})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = Long.parseLong(matcher.group(1));
            }
        }
        return value;
    };

    /**
     * Get the value of a JVM option that specifies a percent with one or more decimal points, with superfluous (past
     * the first decimal place) trailing zeroes removed.
     * 
     * For example:
     * 
     * The value for <code>-XX:MaxRAMPercentage=60.000000</code> is 60.0.
     * 
     * The value for <code>-XX:MaxRAMPercentage=60.100000</code> is 60.1.
     * 
     * The value for <code>-XX:MaxRAMPercentage=60.001000</code> is 60.001.
     * 
     * @param option
     *            The JVM option.
     * @return The JVM option value, or null if the option does not exist.
     */
    public static final String getPercentOptionValue(final String option) {
        String value = null;
        if (option != null) {
            String regex = "^-[a-zA-Z:]+=(\\d{1,3}\\.\\d{1,})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(option);
            if (matcher.find()) {
                value = matcher.group(1);
                if (!value.endsWith(".0$") && value.matches(".+0{1,}$")) {
                    // Remove superfluous trailing zeroes
                    StringBuilder sb = new StringBuilder(value);
                    while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '0' && sb.charAt(sb.length() - 2) != '.') {
                        sb.setLength(sb.length() - 1);
                    }
                    value = sb.toString();
                }
            }
        }
        return value;
    };

    /**
     * Determine if a JVM option is explicitly disabled. For example, <code>-XX:-TraceClassUnloading</code> is disabled.
     * 
     * @param option
     *            The JVM option.
     * @return True if the JVM option is disabled, false otherwise.
     */
    public static final boolean isOptionDisabled(final String option) {
        boolean disabled = false;
        if (option != null) {
            disabled = option.matches("^-XX:-.+$");
        }
        return disabled;
    }

    /**
     * Determine if a JVM option is explicitly enabled. For example, <code>-XX:+TraceClassUnloading</code> is enabled.
     * 
     * @param option
     *            The JVM option.
     * @return True if the JVM option is enabled, false otherwise.
     */
    public static final boolean isOptionEnabled(final String option) {
        boolean enabled = false;
        if (option != null) {
            enabled = option.matches("^-XX:\\+.+$");
        }
        return enabled;
    }

    /**
     * Make default constructor private so the class cannot be instantiated.
     */
    private JdkUtil() {

    }

}
