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
package net.tirasa.connid.bundles.cmd.realenvironment;

import java.util.HashSet;
import java.util.Set;
import net.tirasa.connid.bundles.cmd.CmdConnector;
import net.tirasa.connid.bundles.cmd.search.Operand;
import net.tirasa.connid.bundles.cmd.search.Operator;
import net.tirasa.connid.bundles.cmd.utilities.AttributesTestValue;
import net.tirasa.connid.bundles.cmd.utilities.SharedTestMethods;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
