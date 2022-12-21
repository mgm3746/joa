/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022 Mike Millson                                                                                    *
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
package org.github.joa.domain;

import java.util.List;

/**
 * <p>
 * JVM context information.
 * </p>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 */
/**
 * @author mmillson
 *
 */
public class JvmContext {

    /**
     * Bit type.
     */
    private Bit bit;

    /**
     * Garbage collectors.
     */
    private List<GarbageCollector> collectors;

    /**
     * Container flag.
     */
    private boolean container;

    /**
     * JVM options.
     */
    private String options;

    /**
     * Operating system type.
     */
    private Os os;

    /**
     * JVM major version.
     */
    private int versionMajor;
    /**
     * JVM minor version.
     */
    private int versionMinor;

    /**
     * @param options
     *            The JVM options.
     */
    public JvmContext(String options) {
        // assume 64-bit
        this(options, 0, 0, null, false, null, Bit.UNKNOWN);
    }

    public Bit getBit() {
        return bit;
    }

    public void setBit(Bit bit) {
        this.bit = bit;
    }

    public List<GarbageCollector> getCollectors() {
        return collectors;
    }

    public void setCollectors(List<GarbageCollector> collectors) {
        this.collectors = collectors;
    }

    public boolean isContainer() {
        return container;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

    public JvmContext(String options, int versionMajor) {
        this(options, versionMajor, 0, null, false, null, Bit.UNKNOWN);
    }

    public JvmContext(String options, int versionMajor, int versionMinor) {
        this(options, versionMajor, versionMinor, null, false, null, Bit.UNKNOWN);
    }

    /**
     * @param options
     *            The JVM options.
     * @param versionMajor
     *            The JDK major version
     * @param versionMinor
     *            The JDK minor version.
     * @param collectors
     *            The JDK garbage collectors.
     * @param container
     *            Container flag.
     * @param os
     *            The operating system type.
     * @param bit
     *            The JDK bit type.
     */
    public JvmContext(String options, int versionMajor, int versionMinor, List<GarbageCollector> collectors,
            boolean container, Os os, Bit bit) {
        this.options = options;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.collectors = collectors;
        this.container = container;
        this.os = os;
        this.bit = bit;
    }

}
