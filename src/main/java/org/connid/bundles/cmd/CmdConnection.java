/**
 * ====================
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008-2009 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2011-2013 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License"). You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at https://oss.oracle.com/licenses/CDDL
 * See the License for the specific language governing permissions and limitations
 * under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at https://oss.oracle.com/licenses/CDDL.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdConnection {

    private static CmdConnection cmdConnection = null;

    public static CmdConnection openConnection() {
        if (cmdConnection == null) {
            cmdConnection = new CmdConnection();
        }
        return cmdConnection;
    }

    private CmdConnection() {}
    
    public String execute(final String cmd, final String[] envp) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(cmd, envp);
        return readOutput(proc);
    }
    
    private String readOutput(final Process proc) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }
}
