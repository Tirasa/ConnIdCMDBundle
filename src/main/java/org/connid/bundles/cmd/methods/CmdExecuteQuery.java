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

import java.net.ConnectException;
import java.util.Map;
import java.util.Properties;
import org.connid.bundles.cmd.search.Operand;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;

public class CmdExecuteQuery extends CmdExec {

    private String scriptPath;

    private Operand filter;

    private ResultsHandler resultsHandler;

    public CmdExecuteQuery(final String scriptPath, final Operand filter, final ResultsHandler rh) {
        this.scriptPath = scriptPath;
        this.filter = filter;
        this.resultsHandler = rh;
    }

    public void execQuery() throws ConnectException {
        switch (filter.getOperator()) {
            case EQ:
                String result = exec(scriptPath, createEnv());
                fillUserHandler(result);
                break;
            case SW:
                break;
            case EW:
                break;
            case C:
                break;
            case OR:
                break;
            case AND:
                break;
            default:
                throw new ConnectorException("Wrong Operator");
        }

    }

    private String[] createEnv() {
        String[] arrayAttributes = new String[1];
        arrayAttributes[0] = filter.getAttributeName() + "=" + filter.getAttributeValue();
        return arrayAttributes;
    }

    private void fillUserHandler(final String searchScriptOutput) throws ConnectException {
        if (searchScriptOutput == null || searchScriptOutput.isEmpty()) {
            throw new ConnectException("No results found");
        }

        final Properties attrs = StringUtil.toProperties(searchScriptOutput);

        final ConnectorObjectBuilder bld = new ConnectorObjectBuilder();

        for (Map.Entry attr : attrs.entrySet()) {
            final String name = attr.getKey().toString();
            final String value = attr.getValue().toString();

            if (Name.NAME.equalsIgnoreCase(name) && StringUtil.isNotBlank(value)) {
                bld.setName(value);
            } else if (Uid.NAME.equals(name)) {
                bld.setUid(value);
            } else if (OperationalAttributes.ENABLE_NAME.equals(name)) {
                bld.addAttribute(AttributeBuilder.buildEnabled(Boolean.parseBoolean(value)));
            } else {
                bld.addAttribute(name, value);
            }
        }

        resultsHandler.handle(bld.build());
    }
}
