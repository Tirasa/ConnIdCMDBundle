/**
 * Copyright (C) ${project.inceptionYear} ConnId (connid-dev@googlegroups.com)
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
package net.tirasa.connid.bundles.cmd;

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
                final Map.Entry<Object, Object> entry = StringUtil.toProperties(env).entrySet().iterator().next();
                builder.environment().put(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        final Process proc = builder.start();
        proc.getOutputStream().close();
        return proc;
    }
}
