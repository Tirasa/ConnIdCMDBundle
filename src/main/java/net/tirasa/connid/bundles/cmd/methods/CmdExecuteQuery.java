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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.tirasa.connid.bundles.cmd.CmdConfiguration;
import net.tirasa.connid.bundles.cmd.search.Operand;
import org.identityconnectors.common.Pair;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.Uid;

public class CmdExecuteQuery extends CmdExec {

    private static final Log LOG = Log.getLog(CmdExecuteQuery.class);

    private static final String ITEM_SEPARATOR = "--- NEW SEARCH RESULT ITEM ---";

    private final Operand filter;

    private final ResultsHandler resultsHandler;

    public CmdExecuteQuery(final ObjectClass oc, final CmdConfiguration cmdConfiguration, final Operand filter,
            final ResultsHandler rh) {
        super(oc, cmdConfiguration);

        this.filter = filter;
        this.resultsHandler = rh;
    }

    public void execQuery() throws ConnectException {
        final Process proc;

        if (filter == null) {
            LOG.ok("Full search (no filter) ...");
            proc = exec(cmdConfiguration.getSearchCmdPath(),
                    cmdConfiguration.isServerInfoEnv() ? getConfigurationEnvs(cmdConfiguration) : null);
            readOutput(proc);
        } else {
            LOG.ok("Search with filter {0} ...", filter);
            proc = exec(cmdConfiguration.getSearchCmdPath(), createEnv());
            switch (filter.getOperator()) {
                case EQ:
                case SW:
                case EW:
                case C:
                    readOutput(proc);
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

    private List<Pair<String, String>> createEnv() {
        List<Pair<String, String>> attributes = new ArrayList<>();

        LOG.ok("Creating environment for search with:");
        LOG.ok(CmdConfiguration.OBJECT_CLASS + ": {0}", oc.getObjectClassValue());
        LOG.ok("Query filter {0}= {1}", filter.getAttributeName(), filter.getAttributeValue());

        attributes.add(new Pair<>(filter.getAttributeName(), filter.getAttributeValue()));
        attributes.add(new Pair<>(CmdConfiguration.OBJECT_CLASS, oc.getObjectClassValue()));
        attributes.add(new Pair<>(CmdConfiguration.CMD_OPERATOR, filter.getOperator().toString()));

        if (cmdConfiguration.isServerInfoEnv()) {
            attributes.addAll(getConfigurationEnvs(cmdConfiguration));
        }
        
        return attributes;
    }

    private void fillUserHandler(final String searchScriptOutput) throws ConnectException {
        if (searchScriptOutput == null || searchScriptOutput.isEmpty()) {
            throw new ConnectException("No results found");
        }

        final Properties attrs = StringUtil.toProperties(searchScriptOutput);

        final ConnectorObjectBuilder bld = new ConnectorObjectBuilder();

        for (Map.Entry<Object, Object> attr : attrs.entrySet()) {
            final String name = attr.getKey().toString();
            final String value = attr.getValue().toString();

            if (Name.NAME.equalsIgnoreCase(name) && StringUtil.isNotBlank(value)) {
                bld.setName(value);
            } else if (Uid.NAME.equals(name)) {
                bld.setUid(value);
            } else if (OperationalAttributes.ENABLE_NAME.equals(name)) {
                bld.addAttribute(AttributeBuilder.buildEnabled(Boolean.parseBoolean(value)));
            } else if (OperationalAttributes.PASSWORD_NAME.equals(name)) {
                bld.addAttribute(AttributeBuilder.buildPassword(new GuardedString(value.toCharArray())));
            } else {
                bld.addAttribute(name, value);
            }
        }

        bld.setObjectClass(oc);

        resultsHandler.handle(bld.build());
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
                        LOG.ok("Handle result item {0}", buffer.toString());
                        fillUserHandler(buffer.toString());
                        buffer.delete(0, buffer.length());
                    }
                } else {
                    buffer.append(line).append("\n");
                }
            }

            if (buffer.length() > 0) {
                LOG.ok("Handle result item {0}", buffer.toString());
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
