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
package org.connid.bundles.cmd.realenvironment;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.connid.bundles.cmd.CmdConnector;
import org.connid.bundles.cmd.search.Operand;
import org.connid.bundles.cmd.search.Operator;
import org.connid.bundles.cmd.utilities.AttributesTestValue;
import org.connid.bundles.cmd.utilities.SharedTestMethods;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author developer
 */
public class CmdExecuteQueryTest extends SharedTestMethods {

    private CmdConnector connector = null;

    private Name name = null;

    private Uid newAccount = null;

    private AttributesTestValue attrs = null;

    @Before
    public final void initTest() {
        attrs = new AttributesTestValue();
        connector = new CmdConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());
    }

    @Test
    public final void searchUser() {
        newAccount = connector.create(
                ObjectClass.ACCOUNT, createSetOfAttributes(name, attrs.getPassword(), true), null);
//        assertEquals(name.getNameValue(), newAccount.getUidValue());

        final Set<ConnectorObject> actual = new HashSet<ConnectorObject>();
        connector.executeQuery(ObjectClass.ACCOUNT,
                new Operand(Operator.EQ, Uid.NAME, newAccount.getUidValue(), false), new ResultsHandler() {

            @Override
            public boolean handle(final ConnectorObject connObj) {
                actual.add(connObj);
                return true;
            }
        }, null);
//        for (ConnectorObject connObj : actual) {
//            assertEquals(name.getNameValue(), connObj.getName().getNameValue());
//        }
//        connector.delete(ObjectClass.ACCOUNT, newAccount, null);
    }

    @After
    public final void close() {
        connector.dispose();
    }
}
