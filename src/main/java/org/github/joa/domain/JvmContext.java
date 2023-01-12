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

import java.util.ArrayList;
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
    private Bit bit = Bit.BIT64;

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
     * JVM options.
     */
    private String options;

    /**
     * Operating system type.
     */
    private Os os = Os.UNIDENTIFIED;

    /**
     * JVM major version.
     */
    private int versionMajor = 0;

    /**
     * JVM minor version.
     */
    private int versionMinor = 0;

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

    public Bit getBit() {
        return bit;
    }

    public List<GarbageCollector> getGarbageCollectors() {
        return garbageCollectors;
    }

    public String getOptions() {
        return options;
    }

    public Os getOs() {
        return os;
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

    public void setBit(Bit bit) {
        this.bit = bit;
    }

    public void setContainer(boolean container) {
        this.container = container;
    }

    public void setGarbageCollectors(List<GarbageCollector> garbageCollectors) {
        this.garbageCollectors = garbageCollectors;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public void setVersionMajor(int versionMajor) {
        this.versionMajor = versionMajor;
    }

    public void setVersionMinor(int versionMinor) {
        this.versionMinor = versionMinor;
    }

}
