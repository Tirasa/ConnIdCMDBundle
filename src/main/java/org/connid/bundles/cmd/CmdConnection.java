/*
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://IdentityConnectors.dev.java.net/legal/license.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing the Covered Code, include this
 * CDDL Header Notice in each file
 * and include the License file at identityconnectors/legal/license.txt.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
package org.connid.bundles.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.identityconnectors.common.logging.Log;

public class CmdConnection {

    private static final Log LOG = Log.getLog(CmdConnection.class);

    private static CmdConnection cmdConnection = null;

    public static CmdConnection openConnection() {
        if (cmdConnection == null) {
            cmdConnection = new CmdConnection();
        }
        return cmdConnection;
    }

    private CmdConnection() {}
    
    public String execute(final String cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(cmd);
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
