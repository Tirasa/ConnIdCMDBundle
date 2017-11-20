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
package org.connid.bundles.cmd.methods;

import org.identityconnectors.common.logging.Log;

public class CmdTest extends CmdExec {

    private static final Log LOG = Log.getLog(CmdTest.class);

    private final String scriptPath;

    public CmdTest(final String scriptPath) {
        super(null);

        this.scriptPath = scriptPath;
    }

    public final void test() {
        LOG.info("Executing test on {0}", scriptPath);
        
        waitFor(exec(scriptPath, null));
    }
}
