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
package org.github.joa.util;

import java.util.ResourceBundle;

/**
 * Utility methods and constants.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JoaUtil {

    /**
     * Retrieve the value for a given property file and key.
     * 
     * @param propertyFile
     *            The property file.
     * @param key
     *            The property key.
     * @return The value for the given property file and key.
     */
    public static final String getPropertyValue(String propertyFile, String key) {
        ResourceBundle rb = ResourceBundle.getBundle("META-INF." + propertyFile);
        return rb.getString(key);
    }

    /**
     * Make default constructor private so the class cannot be instantiated.
     */
    private JoaUtil() {

    }
}
