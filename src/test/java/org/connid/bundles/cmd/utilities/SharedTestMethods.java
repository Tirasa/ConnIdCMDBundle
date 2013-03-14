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
package org.connid.bundles.cmd.utilities;

import java.util.Set;
import org.connid.bundles.cmd.CmdConfiguration;
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
