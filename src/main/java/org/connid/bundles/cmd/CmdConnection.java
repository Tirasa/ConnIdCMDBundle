/**
 * Copyright (C) 2011 ConnId (connid-dev@googlegroups.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.connid.bundles.cmd;

import java.io.IOException;
import java.util.List;
import org.identityconnectors.common.Pair;
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

    private CmdConnection() {
    }

    public Process execute(final String path, final List<Pair<String, String>> env) throws IOException {
        LOG.info("Execute script {0} {1}", path, env);

        ProcessBuilder builder = new ProcessBuilder(path.split(" "));
        if (env != null) {
            for (Pair<String, String> entry : env) {
                builder.environment().put(entry.first, entry.second);
            }
        }

        Process proc = builder.start();
        proc.getOutputStream().close();
        return proc;
    }
}
