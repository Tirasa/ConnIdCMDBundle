/**
 * ====================
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
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
package org.connid.bundles.cmd;

import java.util.Set;
import org.connid.bundles.cmd.methods.CmdCreate;
import org.connid.bundles.cmd.methods.CmdDelete;
import org.connid.bundles.cmd.methods.CmdTest;
import org.connid.bundles.cmd.methods.CmdUpdate;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

@ConnectorClass(configurationClass = CmdConfiguration.class, displayNameKey = "cmd.connector.display")
public class CmdConnector implements Connector, CreateOp, UpdateOp, DeleteOp, TestOp {

    private static final Log LOG = Log.getLog(CmdConnector.class);

    private CmdConfiguration cmdConfiguration;

    @Override
    public Configuration getConfiguration() {
        return cmdConfiguration;
    }

    @Override
    public void init(Configuration configuration) {
        cmdConfiguration = (CmdConfiguration) configuration;
    }

    @Override
    public void dispose() {
        //NO
    }

    @Override
    public Uid create(ObjectClass oc,
            Set<Attribute> set, OperationOptions oo) {
        new CmdCreate(cmdConfiguration.getCreateCmdPath(), set).execCreateCmd();
        return new Uid("mas");
    }

    @Override
    public Uid update(ObjectClass oc, Uid uid,
            Set<Attribute> set, OperationOptions oo) {
        new CmdUpdate(cmdConfiguration.getCreateCmdPath(), uid, set).execUpdateCmd();
        return new Uid("mas");
    }

    @Override
    public void delete(ObjectClass oc, Uid uid, OperationOptions oo) {
        new CmdDelete(cmdConfiguration.getDeleteCmdPath(), uid).execDeleteCmd();
    }

    @Override
    public void test() {
        LOG.info("Remote connection test");
        new CmdTest(cmdConfiguration).test();
    }
}
