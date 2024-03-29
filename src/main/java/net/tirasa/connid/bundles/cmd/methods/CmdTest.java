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
package net.tirasa.connid.bundles.cmd.methods;

import java.util.Collections;
import net.tirasa.connid.bundles.cmd.CmdConfiguration;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.Attribute;

public class CmdTest extends CmdExec {

    private static final Log LOG = Log.getLog(CmdTest.class);

    public CmdTest(final CmdConfiguration cmdConfiguration) {
        super(null, cmdConfiguration);
    }

    public final void test() {
        LOG.info("Executing test on {0}", cmdConfiguration.getTestCmdPath());

        waitFor(exec(cmdConfiguration.getTestCmdPath(),
                createEnv(Collections.<Attribute>emptySet(), cmdConfiguration)));
    }
}
