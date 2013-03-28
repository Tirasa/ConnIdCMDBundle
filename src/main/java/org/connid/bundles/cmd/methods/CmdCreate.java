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
package org.connid.bundles.cmd.methods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Uid;

public class CmdCreate extends CmdExec {

    private String scriptPath = null;

    private Set<Attribute> attrs;

    public CmdCreate(final String path, final Set<Attribute> attrs) {
        this.attrs = attrs;
        scriptPath = path;
    }

    public Uid execCreateCmd() {

        final Name name = AttributeUtil.getNameFromAttributes(attrs);

        if (name == null || StringUtil.isBlank(name.getNameValue())) {
            throw new IllegalArgumentException(
                    "No Name attribute provided in the attributes");
        }

        exec(scriptPath, createEnv());
        return new Uid(name.getNameValue());

    }

    private String[] createEnv() {
        final List<String> res = new ArrayList<String>();
        final Iterator attributes = attrs.iterator();

        int index = 0;

        while (attributes.hasNext()) {
            Attribute attribute = (Attribute) attributes.next();
            res.add(attribute.getName() + "=" + attribute.getValue().get(0));
            index++;
        }

        return res.toArray(new String[res.size()]);
    }
}
