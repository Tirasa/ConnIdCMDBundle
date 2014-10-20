/* 
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2011 ConnId. All rights reserved.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 * 
 * You can obtain a copy of the License at
 * http://opensource.org/licenses/cddl1.php
 * See the License for the specific language governing permissions and limitations
 * under the License.
 * 
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://opensource.org/licenses/cddl1.php.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
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

    protected Process exec(final String path, String[] env) {
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

        if (oc != null) {
            res.add("OBJECT_CLASS=" + oc.getObjectClassValue());
        }

        for (Attribute attr : attrs) {
            if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                res.add(attr.getName() + "=" + attr.getValue().get(0));
            }
        }

        if (uid != null && AttributeUtil.find(Uid.NAME, attrs) == null) {
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
