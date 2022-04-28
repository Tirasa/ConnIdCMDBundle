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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest {

    private static File SCRIPT;

    @BeforeAll
    public static void initScript() throws IOException {
        SCRIPT = File.createTempFile("test", ".sh");
        SCRIPT.setExecutable(true);

        try (PrintWriter writer = new PrintWriter(SCRIPT)) {
            writer.write("#!/bin/bash\n");
            writer.write("export name=$__NAME__\n");
            writer.write("export password=$__PASSWORD__\n");
        }
    }

    protected CmdConfiguration createConfiguration() {
        // create the connector configuration..
        CmdConfiguration config = new CmdConfiguration();
        config.setTestCmdPath(SCRIPT.getAbsolutePath());
        config.setCreateCmdPath(SCRIPT.getAbsolutePath());
        config.setUpdateCmdPath(SCRIPT.getAbsolutePath());
        config.setDeleteCmdPath(SCRIPT.getAbsolutePath());
        config.setSearchCmdPath(SCRIPT.getAbsolutePath());
        return config;
    }

    protected Set<Attribute> createSetOfAttributes(final Name name, final String password, final boolean status) {
        GuardedString encPassword = password == null
                ? null
                : new GuardedString(password.toCharArray());

        Set<Attribute> attributes = CollectionUtil.newSet(AttributeBuilder.buildPassword(encPassword));
        attributes.add(AttributeBuilder.buildEnabled(status));
        attributes.add(name);
        return attributes;
    }
}
