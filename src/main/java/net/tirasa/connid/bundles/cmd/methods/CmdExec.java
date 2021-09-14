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
import net.tirasa.connid.bundles.cmd.CmdConfiguration;
import org.identityconnectors.common.Pair;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.common.security.SecurityUtil;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;

public abstract class CmdExec {

    private static final Log LOG = Log.getLog(CmdExec.class);

    protected final ObjectClass oc;
    
    protected final CmdConfiguration cmdConfiguration;

    public CmdExec(final ObjectClass oc, final CmdConfiguration cmdConfiguration) {
        this.oc = oc;
        this.cmdConfiguration = cmdConfiguration;
    }

    protected Process exec(final String path, final List<Pair<String, String>> env) {
        try {
            return CmdConnection.openConnection().execute(path, env);
        } catch (Exception e) {
            LOG.error(e, "Error executing script: " + path);
            throw new ConnectorException(e);
        }
    }

    protected List<Pair<String, String>> createEnv(
            final Set<Attribute> attrs,
            final CmdConfiguration cmdConfiguration) {
        return createEnv(attrs, null, cmdConfiguration);
    }

    protected List<Pair<String, String>> createEnv(
            final Set<Attribute> attrs,
            final Uid uid,
            final CmdConfiguration cmdConfiguration) {
        final List<Pair<String, String>> env = new ArrayList<>();

        LOG.ok("Creating environment with:");
        if (oc != null) {
            LOG.ok(CmdConfiguration.OBJECT_CLASS + ": {0}", oc.getObjectClassValue());
            env.add(new Pair<>(CmdConfiguration.OBJECT_CLASS, oc.getObjectClassValue()));
        }

        for (Attribute attr : attrs) {
            if (attr.getValue() != null && !attr.getValue().isEmpty()) {
                LOG.ok("Environment variable {0}: {1}", attr.getName(), attr.getValue().get(0));

                if (OperationalAttributes.PASSWORD_NAME.equals(attr.getName())) {
                    GuardedString gpasswd = AttributeUtil.getPasswordValue(attrs);
                    if (gpasswd != null) {
                        env.add(new Pair<>(OperationalAttributes.PASSWORD_NAME, SecurityUtil.decrypt(gpasswd)));
                    }
                } else {
                    env.add(new Pair<>(attr.getName(), StringUtil.join(attr.getValue().toArray(), ',')));
                }
            }
        }
        
        if (cmdConfiguration.isServerInfoEnv()) {
            env.addAll(getConfigurationEnvs(cmdConfiguration));
        }
        
        if (uid != null && AttributeUtil.find(Uid.NAME, attrs) == null) {
            LOG.ok("Environment variable {0}: {1}", Uid.NAME, uid.getUidValue());
            env.add(new Pair<>(Uid.NAME, uid.getUidValue()));
        }

        return env;

    }

    protected List<Pair<String, String>> getConfigurationEnvs(final CmdConfiguration cmdConfiguration) {
        final List<Pair<String, String>> confEnvs = new ArrayList<>();

        LOG.ok("Creating configuration environment with:");

        if (StringUtil.isNotEmpty(cmdConfiguration.getHost())) {
            confEnvs.add(new Pair<>(CmdConfiguration.CMD_HOST, cmdConfiguration.getHost()));
        }
        if (StringUtil.isNotEmpty(cmdConfiguration.getPort())) {
            confEnvs.add(new Pair<>(CmdConfiguration.CMD_PORT, cmdConfiguration.getPort()));
        }
        if (StringUtil.isNotEmpty(cmdConfiguration.getUser())) {
            confEnvs.add(new Pair<>(CmdConfiguration.CMD_USER, cmdConfiguration.getUser()));
        }
        if (StringUtil.isNotEmpty(cmdConfiguration.getPrivateKeyPath())) {
            confEnvs.add(new Pair<>(CmdConfiguration.CMD_PRIVATE_KEY_PATH, cmdConfiguration.getPrivateKeyPath()));
        }
        return confEnvs;
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
