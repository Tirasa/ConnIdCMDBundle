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
package net.tirasa.connid.bundles.cmd;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CmdCreateTest extends AbstractTest {

    private CmdConnector connector;

    private Name name;

    private AttributesTestValue attrs;

    @BeforeEach
    public void initTest() {
        attrs = new AttributesTestValue();
        connector = new CmdConnector();
        connector.init(createConfiguration());
        name = new Name(attrs.getUsername());

        connector.init(createConfiguration());
    }

    @AfterEach
    public void dispose() {
        connector.dispose();
    }

    @Test
    public void create() {
        connector.create(
                ObjectClass.ACCOUNT,
                createSetOfAttributes(name, attrs.getPassword(), true),
                new OperationOptionsBuilder().build());
    }

    @Test
    public void issueCMD7() {
        connector.create(
                ObjectClass.ACCOUNT,
                createSetOfAttributes(name, "x1:x?x}Xxxx\\", true),
                new OperationOptionsBuilder().build());
    }
}
