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
package org.connid.bundles.cmd.methods;

import java.io.IOException;
import org.connid.bundles.cmd.CmdConfiguration;
import org.connid.bundles.cmd.CmdConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class CmdTest {
    
    private static final Log LOG = Log.getLog(CmdTest.class);

    private CmdConnection unixConnection = null;
    private CmdConfiguration cmdConfiguration = null;

    public CmdTest(final CmdConfiguration cmdConfiguration) {
        unixConnection = CmdConnection.openConnection();
        this.cmdConfiguration = cmdConfiguration;
    }

    public final void test() {
        try {
            execute();
        } catch (Exception e) {
            LOG.error(e, "error during test connection");
            throw new ConnectorException(e);
        }
    }

    private void execute() throws IOException {
        unixConnection.execute(cmdConfiguration.getTestCmdPath());
    }
    
}
