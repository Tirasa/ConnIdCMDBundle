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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.tirasa.connid.bundles.cmd.CmdConnection;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;

public abstract class CmdExec {

    private static final Log LOG = Log.getLog(CmdExec.class);

    protected final ObjectClass oc;

    public CmdExec(final ObjectClass oc) {
        this.oc = oc;
    }

    protected Process exec(final String path, final String[] env) {
        try {
            return CmdConnection.openConnection().execute(path, env);
        } catch (Exception e) {
            LOG.error(e, "Error executing script: " + path);
            throw new ConnectorException(e);
        }
    }

    protected String[] createEnv(final Set<Attribute> attrs) {
        return createEnv(attrs, null);
    }

    protected String[] createEnv(final Set<Attribute> attrs, final Uid uid) {
        final List<String> res = new ArrayList<String>();

        LOG.ok("Creating environment with:");
        if (oc != null) {
            LOG.ok("OBJECT_CLASS: {0}", oc.getObjectClassValue());
            res.add("OBJECT_CLASS=" + oc.getObjectClassValue());
        }

        for (final Attribute attr : attrs) {
            if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                LOG.info("   > Attr: {0}", attr.getName() + "=" + attr.getValue().get(0));
                res.add(attr.getName() + "=" + attr.getValue().get(0));
            }
        }

        if (uid != null && AttributeUtil.find(Uid.NAME, attrs) == null) {
            LOG.ok("Environment variable {0}: {1}", Uid.NAME, uid.getUidValue());
            res.add(Uid.NAME + "=" + uid.getUidValue());
        }

        return res.toArray(new String[res.size()]);
    }

    protected void waitFor(final Process proc) {
        try {
            proc.waitFor();
            LOG.info("Process ended");
        } catch (InterruptedException e) {
            LOG.error(e, "Error waiting for termination");
        }
    }
}
