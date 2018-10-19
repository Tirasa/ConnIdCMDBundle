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
package net.tirasa.connid.bundles.cmd;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.junit.Test;

public class CmdTest extends AbstractTest {

    @Test
    public final void testConnection() {
        final CmdConnector connector = new CmdConnector();
        connector.init(createConfiguration());
        connector.test();
        connector.dispose();
    }

    @Test(expected = ConnectorException.class)
    public final void testWrongConnection() {
        final CmdConnector connector = new CmdConnector();
        CmdConfiguration cmdConfiguration = createConfiguration();
        cmdConfiguration.setTestCmdPath("/tmp/wrong.sh");
        connector.init(cmdConfiguration);
        connector.test();
        connector.dispose();
    }
}
