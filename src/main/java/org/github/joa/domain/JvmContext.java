/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2024 Mike Millson                                                                                    *
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

import java.util.ArrayList;
import java.util.Date;
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
     * int value to use when the value is unknown.
     */
    public static final int UNKNOWN = Integer.MIN_VALUE;

    /**
     * JVM architecture.
     */
    private Arch arch = Arch.UNKNOWN;

    /**
     * Bit type.
     */
    private Bit bit = Bit.BIT64;

    /**
     * Build date/time.
     */
    private Date buildDate;

    /**
     * JVM builder.
     */
    private BuiltBy builtBy = BuiltBy.UNKNOWN;

    /**
     * Container flag.
     */
    private boolean container = false;

    /**
     * Identified garbage collectors (not just expected). It should agree with the JVM options unless options are
     * overriden or ignored for some reason (e.g. nonsense values).
     */
    List<GarbageCollector> garbageCollectors = new ArrayList<>();

    /**
     * The OS/container physical memory in <code>Constants.PRECISION_REPORTING</code> units.
     */
    private long memory;

    /**
     * JVM options.
     */
    private String options;

    /**
     * Operating system type.
     */
    private Os os = Os.UNIDENTIFIED;

    /**
     * Release string. Includes major and minor version. For example:
     * 
     * <pre>
     * 1.8.0_332-b09-1
     * 11.0.15+9-LTS-1
     * 17.0.3+6-LTS-2
     * </pre>
     */
    private String releaseString;

    /**
     * JVM major version.
     */
    private int versionMajor = UNKNOWN;

    /**
     * JVM minor version.
     */
    private int versionMinor = UNKNOWN;

    /**
     * @param options
     *            The JVM options.
     */
    public JvmContext(String options) {
        this.options = options;
    }

    public JvmContext(String options, int versionMajor) {
        this.options = options;
        this.versionMajor = versionMajor;
    }

    public JvmContext(String options, int versionMajor, int versionMinor) {
        this.options = options;
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
    }

    public Arch getArch() {
        return arch;
    }

    public Bit getBit() {
        return bit;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public BuiltBy getBuiltBy() {
        return builtBy;
    }

    public List<GarbageCollector> getGarbageCollectors() {
        return garbageCollectors;
    }

    public long getMemory() {
        return memory;
    }

    public String getOptions() {
        return options;
    }

    public Os getOs() {
        return os;
    }

    public String getReleaseString() {
        return releaseString;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public boolean isContainer() {
        return container;
    }

    public void setArch(Arch arch) {
        this.arch = arch;
    }

    public void setBit(Bit bit) {
        this.bit = bit;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public void setBuiltBy(BuiltBy builtBy) {
        this.builtBy = builtBy;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public void setGarbageCollectors(List<GarbageCollector> garbageCollectors) {
        this.garbageCollectors = garbageCollectors;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public void setReleaseString(String releaseString) {
        this.releaseString = releaseString;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }
}
