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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.connid.bundles.cmd.search.Operand;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;

public class CmdExecuteQuery extends CmdExec {

    private static final Log LOG = Log.getLog(CmdExecuteQuery.class);

    private static String ITEM_SEPARATOR = "--- NEW SEARCH RESULT ITEM ---";

    private String scriptPath;

    private Operand filter;

    private ResultsHandler resultsHandler;

    public CmdExecuteQuery(final ObjectClass oc, final String scriptPath, final Operand filter, final ResultsHandler rh) {
        super(oc);

        this.scriptPath = scriptPath;
        this.filter = filter;
        this.resultsHandler = rh;
    }

    public void execQuery() throws ConnectException {
        final Process proc;

        if (filter == null) {
            LOG.info("Full search (no filter) ...");
            proc = exec(scriptPath, null);
            readOutput(proc);
        } else {
            LOG.info("Search with filter {0} ...", filter);
            proc = exec(scriptPath, createEnv());
            switch (filter.getOperator()) {
                case EQ:
                    readOutput(proc);
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

        waitFor(proc);
    }

    private String[] createEnv() {
        final List<String> attributes = new ArrayList<String>();

        attributes.add(filter.getAttributeName() + "=" + filter.getAttributeValue());
        attributes.add("OBJECT_CLASS=" + oc.getObjectClassValue());

        return attributes.toArray(new String[attributes.size()]);
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

        bld.setObjectClass(oc);

        final ConnectorObject connObject = bld.build();

        resultsHandler.handle(connObject);
    }

    private void readOutput(final Process proc) {
        LOG.info("Read for script output ...");

        final BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        final StringBuilder buffer = new StringBuilder();

        String line;

        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(ITEM_SEPARATOR)) {
                    if (buffer.length() > 0) {
                        LOG.info("Handle result item {0}", buffer.toString());
                        fillUserHandler(buffer.toString());
                        buffer.delete(0, buffer.length());
                    }
                } else {
                    buffer.append(line).append("\n");
                }
            }

            if (buffer.length() > 0) {
                LOG.info("Handle result item {0}", buffer.toString());
                fillUserHandler(buffer.toString());
            }

        } catch (IOException e) {
            LOG.error(e, "Error reading result items");
        }

        try {
            br.close();
        } catch (IOException e) {
            LOG.ok(e, "Error closing reader");
        }
    }
}
