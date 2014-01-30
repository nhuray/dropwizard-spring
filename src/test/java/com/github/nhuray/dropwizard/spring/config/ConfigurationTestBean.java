package com.github.nhuray.dropwizard.spring.config;

import ch.qos.logback.classic.Level;

public class ConfigurationTestBean {

    private String connectorType;
    private String rootPath;

    private Level loggingLevel;

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Level getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(Level loggingLevel) {
        this.loggingLevel = loggingLevel;
    }
}
