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
package net.tirasa.connid.bundles.cmd.utilities;

import java.util.Set;
import net.tirasa.connid.bundles.cmd.CmdConfiguration;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.Name;

public class SharedTestMethods {

    protected final CmdConfiguration createConfiguration() {
        // create the connector configuration..
        CmdConfiguration config = new CmdConfiguration();
        config.setTestCmdPath("/tmp/test.sh");
        config.setCreateCmdPath("/tmp/test.sh");
        config.setSearchCmdPath("/tmp/test.sh");
        return config;
    }

    protected final Set<Attribute> createSetOfAttributes(final Name name,
            final String password, final boolean status) {
        AttributesTestValue attrs = new AttributesTestValue();
        GuardedString encPassword = null;
        if (password != null) {
            encPassword = new GuardedString(password.toCharArray());
        }

        final Set<Attribute> attributes = CollectionUtil.newSet(
                AttributeBuilder.buildPassword(encPassword));
        attributes.add(AttributeBuilder.buildEnabled(status));
        attributes.add(AttributeBuilder.build("comment", CollectionUtil.newSet(
                attrs.getUsername())));
        attributes.add(AttributeBuilder.build("shell", CollectionUtil.newSet(
                "/bin/rbash")));
        attributes.add(name);
        return attributes;
    }
}
