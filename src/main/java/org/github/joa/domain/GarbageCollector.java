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
package org.github.joa.domain;

/**
 * <p>
 * Defined garbage collectors.
 * </p>
 * 
 * <p>
 * Default "server" collector:
 * </p>
 * 
 * <ul>
 * <li>JDK8: PARALLEL_SCAVENGE, PARALLEL_OLD</li>
 * <li>JDK11: G1</li>
 * <li>JDK17: G1</li>
 * </ul>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 */
public enum GarbageCollector {
    CMS, G1, PAR_NEW, PARALLEL_OLD, PARALLEL_SCAVENGE, SERIAL, SERIAL_OLD, SHENANDOAH, UNKNOWN, ZGC;
}
