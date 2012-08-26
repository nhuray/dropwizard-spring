package com.github.nhuray.dropwizard.spring.config;

public class ConfigurationTestBean {

    private String connectorType;
    private String rootPath;

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
}