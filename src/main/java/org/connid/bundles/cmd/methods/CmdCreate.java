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

import java.util.Set;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;

public class CmdCreate extends CmdExec {

    private static final Log LOG = Log.getLog(CmdCreate.class);

    private final String scriptPath;

    private final Set<Attribute> attrs;

    public CmdCreate(final ObjectClass oc, final String path, final Set<Attribute> attrs) {
        super(oc);

        this.attrs = attrs;
        scriptPath = path;
    }

    public Uid execCreateCmd() {
        Name name = AttributeUtil.getNameFromAttributes(attrs);
        if (name == null || StringUtil.isBlank(name.getNameValue())) {
            throw new IllegalArgumentException("No Name provided in the attributes");
        }
        LOG.info("Executing creation for {0}", name.getNameValue());

        waitFor(exec(scriptPath, createEnv(attrs)));

        return new Uid(name.getNameValue());
    }
}
