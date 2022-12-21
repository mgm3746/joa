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
     * 64-bit flag.
     */
    private boolean bit64;

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
    private OsType osType;

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
        this(options, 0, 0, null, true, false, null);
    }

    public JvmContext(String options, int versionMajor, int versionMinor) {
        // assume 64-bit
        this(options, versionMajor, versionMinor, null, true, false, null);
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
     * @param bit64
     *            64-bit JDK flag.
     * @param container
     *            Container flag.\
     * @param osType
     *            The operating system type.
     */
    public JvmContext(String options, int versionMajor, int versionMinor, List<GarbageCollector> collectors,
            boolean bit64, boolean container, OsType osType) {
        this.options = options;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.collectors = collectors;
        this.bit64 = bit64;
        this.container = container;
        this.osType = osType;
    }

    public List<GarbageCollector> getCollectors() {
        return collectors;
    }

    public String getOptions() {
        return options;
    }

    public OsType getOsType() {
        return osType;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public boolean isBit64() {
        return bit64;
    }

    public boolean isContainer() {
        return container;
    }

    public void setBit64(boolean bit64) {
        this.bit64 = bit64;
    }

    public void setCollectors(List<GarbageCollector> collectors) {
        this.collectors = collectors;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }
}
