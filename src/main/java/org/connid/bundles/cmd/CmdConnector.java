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
package org.connid.bundles.cmd;

import java.net.ConnectException;
import java.util.Map;
import java.util.Set;
import org.connid.bundles.cmd.methods.CmdCreate;
import org.connid.bundles.cmd.methods.CmdDelete;
import org.connid.bundles.cmd.methods.CmdExecuteQuery;
import org.connid.bundles.cmd.methods.CmdTest;
import org.connid.bundles.cmd.methods.CmdUpdate;
import org.connid.bundles.cmd.search.Operand;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

@ConnectorClass(configurationClass = CmdConfiguration.class, displayNameKey = "cmd.connector.display")
public class CmdConnector implements Connector, CreateOp, UpdateOp, DeleteOp, TestOp, SearchOp<Operand> {

    private static final Log LOG = Log.getLog(CmdConnector.class);

    private CmdConfiguration cmdConfiguration;

    @Override
    public Configuration getConfiguration() {
        return cmdConfiguration;
    }

    @Override
    public void init(final Configuration configuration) {
        cmdConfiguration = (CmdConfiguration) configuration;
    }

    @Override
    public void dispose() {
        //NO
    }

    @Override
    public Uid create(final ObjectClass oc, final Set<Attribute> attributes, final OperationOptions oo) {
        if (LOG.isOk()) {
            LOG.ok("Create parameters:");
            LOG.ok("ObjectClass {0}", oc.getObjectClassValue());

            for (Attribute attr : attributes) {
                LOG.ok("Attribute {0}: {1}", attr.getName(), attr.getValue());
            }
            for (final Map.Entry<String, Object> entrySet : oo.getOptions().entrySet()) {
                final String key = entrySet.getKey();
                final Object value = entrySet.getValue();
                LOG.ok("OperationOptions {0}: {1}", key, value);
            }
        }

        return new CmdCreate(oc, cmdConfiguration.getCreateCmdPath(), attributes).execCreateCmd();
    }

    @Override
    public Uid update(final ObjectClass oc, final Uid uid, final Set<Attribute> attributes, final OperationOptions oo) {
        if (LOG.isOk()) {
            LOG.ok("Update parameters:");
            LOG.ok("ObjectClass {0}", oc.getObjectClassValue());
            LOG.ok("Uid: {0}", uid.getUidValue());
            for (Attribute attr : attributes) {
                LOG.ok("Attribute {0}: {1}", attr.getName(), attr.getValue());
            }
            for (Map.Entry<String, Object> entrySet : oo.getOptions().entrySet()) {
                LOG.ok("   > OperationOptions {0}", entrySet.getKey() + ": " + entrySet.getValue());
            }
        }

        return new CmdUpdate(oc, cmdConfiguration.getUpdateCmdPath(), uid, attributes).execUpdateCmd();
    }

    @Override
    public void delete(final ObjectClass oc, final Uid uid, final OperationOptions oo) {
        if (LOG.isOk()) {
            LOG.ok("Delete parameters:");
            LOG.ok("ObjectClass {0}", oc.getObjectClassValue());
            LOG.ok("Uid: {0}", uid.getUidValue());
            for (Map.Entry<String, Object> entrySet : oo.getOptions().entrySet()) {
                LOG.ok("OperationOptions {0}: {1}", entrySet.getKey(), entrySet.getValue());
            }
        }

        new CmdDelete(oc, cmdConfiguration.getDeleteCmdPath(), uid).execDeleteCmd();
    }

    @Override
    public void test() {
        LOG.ok("Remote connection test");
        new CmdTest(cmdConfiguration.getTestCmdPath()).test();
    }

    @Override
    public void executeQuery(
            final ObjectClass oc,
            final Operand t,
            final ResultsHandler rh,
            final OperationOptions oo) {

        if (LOG.isOk()) {
            LOG.ok("Search parameters:");
            LOG.ok("ObjectClass {0}", oc.getObjectClassValue());
            LOG.ok("Operand is uid: {0}", t.isUid());
            for (Map.Entry<String, Object> entrySet : oo.getOptions().entrySet()) {
                LOG.ok("OperationOptions {0}: {1}", entrySet.getKey(), entrySet.getValue());
            }
        }

        try {
            new CmdExecuteQuery(oc, cmdConfiguration.getSearchCmdPath(), t, rh).execQuery();
        } catch (ConnectException ex) {
            LOG.error("Error in connection process", ex);
        }
    }

    @Override
    public FilterTranslator<Operand> createFilterTranslator(final ObjectClass oc, final OperationOptions oo) {
        if (oc == null || (!oc.equals(ObjectClass.ACCOUNT)) && (!oc.equals(ObjectClass.GROUP))) {
            throw new IllegalArgumentException("Invalid objectclass");
        }
        return new CmdFilterTranslator();
    }
}
