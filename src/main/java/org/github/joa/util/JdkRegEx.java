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
package org.github.joa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expression utility methods and constants for OpenJDK.
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class JdkRegEx {
    /**
     * File or directory name. Cannot begin/end with a space.
     * 
     * For example:
     * 
     * <p>
     * 1) Linux/Unix:
     * </p>
     * 
     * <pre>
     * libaio.so.1.0.1
     * mylibrary
     * my-library
     * my library
     * [aio]
     * </pre>
     * 
     * <p>
     * 2) Windows:
     * </p>
     * 
     * <pre>
     * jvm.dll
     * my library.dll
     * </pre>
     */
    public static final String DIR_FILE = "[A-Za-z0-9\\.\\-\\+_\\$@~:()\\[\\]%]+( [A-Za-z0-9\\.\\-\\+_\\$@~:()%]+)*";

    /**
     * File path.
     * 
     * For example:
     * 
     * <p>
     * 1) Linux/Unix:
     * </p>
     * 
     * <pre>
     * /usr/lib64/
     * /usr/lib64/libaio.so.1.0.1
     * mylibrary
     * </pre>
     * 
     * <p>
     * 2) Windows:
     * </p>
     * 
     * <pre>
     * E:\path\java\bin\server\
     * E:\path\java\bin\server\jvm.dll
     * mylibrary.dll
     * </pre>
     */
    public static final String FILE_PATH = "([A-Z]:\\\\|/)?(" + DIR_FILE + "([/\\\\])?)+";

    /**
     * A single JVM option.
     */
    public static final String JVM_OPTION = "-(-add|agentlib|agentpath|classpath|client|d(32|64)|javaagent|noverify|"
            + "server|verbose|D|X)";

    /**
     * A string of JVM options.
     *
     * (?&lt;!^) match whatever follows, but not the start of the string
     *
     * (?= ) match "space" followed by jvm option, but don't include the space in the leading space result
     * 
     * Can be used to split JVM options String s into individual options:
     * 
     * String[] options = s.split(JdkRegEx.JVM_OPTIONS);
     */
    public static final String JVM_OPTIONS = "(?<!^)(?= " + JVM_OPTION + ")";

    /**
     * Units for JVM options that take a byte number.
     * 
     * For example: -Xss128k -Xmx2048m -Xms2G -XX:ThreadStackSize=256
     */
    public static final String OPTION_SIZE_BYTES = "((\\d{1,})(b|B|k|K|m|M|g|G)?)";

    /**
     * Get file from file path.
     * 
     * @param filePath
     *            The file path.
     * @return The file name, or null if it is a directory.
     */
    public static final String getFile(final String filePath) {
        String file = null;
        if (filePath != null && !filePath.matches("^.+[/\\\\]$")) {
            Pattern pattern = Pattern.compile(FILE_PATH);
            Matcher matcher = pattern.matcher(filePath);
            if (matcher.find()) {
                file = matcher.group(matcher.groupCount() - 2);
            }
        }
        return file;
    }

}
