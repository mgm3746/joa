/**********************************************************************************************************************
 * JVM Options Analyzer                                                                                               *
 *                                                                                                                    *
 * Copyright (c) 2022-2025 Mike Millson                                                                                    *
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
 * Defined builder strings.
 * </p>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 */
public enum BuiltBy {
    // Red Hat
    BUILD,
    // Graal
    BUILDSLAVE,
    // In some Oracle and Red Hat builds
    EMPTY,
    // Oracle: "Java Release Engineering"
    JAVA_RE,
    // AdoptOpenJDK
    JENKINS,
    // Oracle: "mach5one" is the name of their build/test system? Replaced by "java_re"?
    MACH5ONE,
    // Red Hat, CentOS
    MOCKBUILD,
    // Adoptium temurin
    TEMURIN,
    // Azul
    TESTER, UNKNOWN,
    // Microsoft
    VSTS,
    // Azul
    ZULU_RE
}
