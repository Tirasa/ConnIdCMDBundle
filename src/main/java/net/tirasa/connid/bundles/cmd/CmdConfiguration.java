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

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

public class CmdConfiguration extends AbstractConfiguration {

    public static final String OBJECT_CLASS = "OBJECT_CLASS";

    public static final String CMD_HOST = "CMD_HOST";

    public static final String CMD_PORT = "CMD_PORT";

    public static final String CMD_PRIVATE_KEY_PATH = "CMD_PRIVATE_KEY_PATH";

    public static final String CMD_USER = "CMD_USER";

    public static final String CMD_OPERATOR = "Operator";

    private String host;

    private String port;

    private String user;

    private boolean serverInfoEnv;

    private String privateKeyPath;

    private String createCmdPath = "";

    private String updateCmdPath = "";

    private String searchCmdPath = "";

    private String deleteCmdPath = "";

    private String testCmdPath = "";

    @ConfigurationProperty(displayMessageKey = "cmd.host.display",
            helpMessageKey = "cmd.host.help", order = 1)
    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.port.display",
            helpMessageKey = "cmd.port.help", order = 2)
    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.user.display",
            helpMessageKey = "cmd.user.help", order = 3)
    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.privateKeyPath.display",
            helpMessageKey = "cmd.privateKeyPath.help", order = 4)
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(final String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.serverInfoEnv.display",
            helpMessageKey = "cmd.serverInfoEnv.help", order = 5)
    public boolean isServerInfoEnv() {
        return serverInfoEnv;
    }

    public void setServerInfoEnv(boolean serverInfoEnv) {
        this.serverInfoEnv = serverInfoEnv;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.createCmdPath.display",
            helpMessageKey = "cmd.createCmdPath.help", order = 5)
    public String getCreateCmdPath() {
        return createCmdPath;
    }

    public void setCreateCmdPath(final String createCmdPath) {
        this.createCmdPath = createCmdPath;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.updateCmdPath.display",
            helpMessageKey = "cmd.updateCmdPath.help", order = 6)
    public String getUpdateCmdPath() {
        return updateCmdPath;
    }

    public void setUpdateCmdPath(String updateCmdPath) {
        this.updateCmdPath = updateCmdPath;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.searchCmdPath.display",
            helpMessageKey = "cmd.updateCmdPath.help", order = 7)
    public String getSearchCmdPath() {
        return searchCmdPath;
    }

    public void setSearchCmdPath(String searchCmdPath) {
        this.searchCmdPath = searchCmdPath;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.deleteCmdPath.display",
            helpMessageKey = "cmd.deleteCmdPath.help", order = 8)
    public String getDeleteCmdPath() {
        return deleteCmdPath;
    }

    public void setDeleteCmdPath(String deleteCmdPath) {
        this.deleteCmdPath = deleteCmdPath;
    }

    @ConfigurationProperty(displayMessageKey = "cmd.testCmdPath.display",
            helpMessageKey = "cmd.testCmdPath.help", order = 9)
    public String getTestCmdPath() {
        return testCmdPath;
    }

    public void setTestCmdPath(String testCmdPath) {
        this.testCmdPath = testCmdPath;
    }

    @Override
    public void validate() {
        if (StringUtil.isBlank(createCmdPath)) {
            throw new ConfigurationException("Create cmd path must not be blank!");
        }
        if (StringUtil.isBlank(updateCmdPath)) {
            throw new ConfigurationException("Update cmd path must not be blank!");
        }
        if (StringUtil.isBlank(searchCmdPath)) {
            throw new ConfigurationException("Search cmd path must not be blank!");
        }
        if (StringUtil.isBlank(deleteCmdPath)) {
            throw new ConfigurationException("Delete cmd path must not be blank!");
        }
    }

}
