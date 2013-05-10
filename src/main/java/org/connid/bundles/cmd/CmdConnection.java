/**
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;

public class CmdConnection {

    private static final Log LOG = Log.getLog(CmdConnector.class);

    private static CmdConnection cmdConnection = null;

    public static CmdConnection openConnection() {
        if (cmdConnection == null) {
            cmdConnection = new CmdConnection();
        }
        return cmdConnection;
    }

    private CmdConnection() {
    }

    public Process execute(final String cmd, final String[] envp) throws IOException {
        LOG.info("Execute script {0} {1}", cmd, Arrays.asList(envp == null ? new String[0] : envp));

        final ProcessBuilder builder = new ProcessBuilder(cmd.split(" "));
        
        if (envp != null) {
            for (String env : envp) {
                final Map.Entry entry = StringUtil.toProperties(env).entrySet().iterator().next();
                builder.environment().put(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        final Process proc = builder.start();
        proc.getOutputStream().close();
        return proc;
    }
}
